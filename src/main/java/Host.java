import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for describing host.
 */
public class Host {
    /**
     * Name of the host. IP address or domain name.
     */
    String name;
    /**
     * User name for connecting via SSH.
     */
    String user;
    /**
     * Password for connecting via SSH.
     */
    String password;
    /**
     * Community string for connecting via SNMP.
     */
    String community;
    /**
     * List of allowed protocols.
     */
    ArrayList<String> protocols;
    /**
     * List of ports in host.
     */
    HashMap<Integer,Port> ports;
    /**
     * WAN port in host.
     */
    Port WAN;
    /**
     * LAN port in host.
     */
    Port LAN;
    /**
     * Object logic for watching activity in host.
     */
    Logic logic;

    /**
     * Constructor for host in the system.
     * @param name name of the host. IP address or domain name.
     * @param user user name for connecting via SSH.
     * @param password password for connecting via SSH.
     * @param community community string for connecting via SNMP.
     * @param protocols list of allowed protocols.
     */
    public Host(String name, String user, String password, String community, ArrayList<String> protocols) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.community = community;
        this.protocols = protocols;
        getPorts();
        cleanMaps();
        makeLogic();
    }

    /**
     * Method for getting name of the host.
     * @return name of the host.
     */
    public String getName() {
        return name;
    }

    /**
     * Method for getting user name for connecting via SSH.
     * @return user name for connecting via SSH.
     */
    public String getUser() {
        return user;
    }

    /**
     * Method for getting password for connecting via SSH.
     * @return password for connecting via SSH.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method for getting community string for connecting via SNMP.
     * @return community string for connecting via SNMP.
     */
    public String getCommunity() {
        return community;
    }

    /**
     * Method for getting list of allowed protocols.
     * @return list of allowed protocols.
     */
    public ArrayList<String> getProtocols() {
        return protocols;
    }

    /**
     * Method to get WAN and LAN port and their timestamp via SNMP.
     */
    public void getPorts() {
        SNMPClient snmpClient = new SNMPClient(name, community,"161");
        try
        {
            snmpClient.start();
            ports = snmpClient.getInterfaceTable(".1.3.6.1.2.1.2.2");
            snmpClient.stop();
            snmpClient.start();
            snmpClient.updateTrafficTable(".1.3.6.1.4.1.9.9.244.1.2.1", ports);
            snmpClient.stop();
        }
        catch (Exception ee){
            System.out.println(ee);
        }
        for (Map.Entry<Integer,Port> entry: ports.entrySet()) {
            Port port = entry.getValue();
            if (port.snapshotList.get(port.snapshotList.size()-1).getIfAlias()!=null&&port.snapshotList.get(port.snapshotList.size()-1).getIfAlias().equals("WAN")){
                WAN = port;
            }
            else if (port.snapshotList.get(port.snapshotList.size()-1).getIfAlias()!=null&&port.snapshotList.get(port.snapshotList.size()-1).getIfAlias().equals("LAN")){
                LAN = port;
            }
        }
    }

    /**
     * Method to run logic and watch for network activity
     */
    public void makeLogic() {
        logic = new Logic(this,WAN,LAN,80, 50);
    }

    /**
     * Method to clean maps when program restarting or has been terminated.
     */
    private void cleanMaps(){
        SshClient ssh = new SshClient(user, name, password);
        ArrayList<String> results = ssh.executeCommand("show running-config | section class-map");
        ArrayList<ClassMap> classMaps = new ArrayList<ClassMap>();
        for (int i = 0; i<results.size();i++) {
            String str = results.get(i);
            if (str.contains("class-map")){
                String[] test = str.split(" ");
                test[2].replace("CMAP","");
                if (isData(test[2].replace("CMAP",""))){
                    String str1 = results.get(i+1);
                    if (str1.contains(" match protocol ")){
                        String[] test1 = str1.split(" ");
                        classMaps.add(new ClassMap(test[1].replace("match-",""),test[2],test1[3]));
                    }
                }
            }
        }
        ArrayList<String> results1 = ssh.executeCommand("show running-config | section policy-map");
        ArrayList<PolicyMap> policyMaps = new ArrayList<PolicyMap>();
        for (int i = 0; i<results1.size();i++) {
            String str = results1.get(i);
            if (str.contains("policy-map")){
                String[] test = str.split(" ");
                test[1].replace("PMAP","");
                if (isData(test[1].replace("PMAP",""))){
                    PolicyMap PMAP = new PolicyMap(test[1],null,0);
                    PMAP.setReverceCommands(makeRevercePMAP(PMAP));
                    policyMaps.add(PMAP);
                }
            }
        }
        for (ClassMap CMAP: classMaps
                ) {
            try {
                ssh.RunCommands(makeReverceCMAP(CMAP));
            } catch (Exception ee) {
                System.out.println(ee);
            }
        }
        for (PolicyMap PMAP: policyMaps
             ) {
            try {
                ssh.RunCommands(PMAP.getReverceCommands());
            } catch (Exception ee) {
                System.out.println(ee);
            }
        }
    }

    /**
     * Method to identify string as Data type. Data type provides 14 digits number.
     * @param s name of the map without "CMAP" or "PMAP".
     * @return result of identification.
     */
    private boolean isData(String s)
    {
        if (s.length()==14) {
            for (int i = 0; i < 14; i++)
                if (Character.isDigit(s.charAt(i))
                        == false)
                    return false;
        }
        else return false;
        return true;
    }

    /**
     * Method to make a list of commands to remove PMAP.
     * @param PMAP PMAP which may be deleted.
     * @return list of commands.
     */
    private ArrayList<String> makeRevercePMAP(PolicyMap PMAP) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        commands.add("no policy-map " + PMAP.getName());
        commands.add("interface " + LAN.snapshotList.get(LAN.snapshotList.size() - 1).getIfDescr());
        commands.add("no service-policy output " + PMAP.getName());
        commands.add("exit");
        commands.add("exit");
        return commands;
    }

    /**
     * Method to make a list of commands to remove CMAP.
     * @param CMAP CMAP which may be deleted.
     * @return list of commands.
     */
    private ArrayList<String> makeReverceCMAP(ClassMap CMAP) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        commands.add("no class-map " + CMAP.getName());
        commands.add("exit");
        return commands;
    }
}
