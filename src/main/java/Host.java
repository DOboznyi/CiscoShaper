import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Host {
    String name;
    String user;
    String password;
    String community;
    ArrayList<String> protocols;
    HashMap<Integer,Port> ports;
    Port WAN;
    Port LAN;
    Logic logic;

    public Host(String name, String user, String password, String community, ArrayList<String> protocols) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.community = community;
        this.protocols = protocols;
        getPorts();
        getPolicyMaps();
        makeLogic();
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getCommunity() {
        return community;
    }

    public ArrayList<String> getProtocols() {
        return protocols;
    }

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

    public void makeLogic() {
        logic = new Logic(this,WAN,LAN,80, 50);
    }

    private ArrayList<PolicyMap> getPolicyMaps(){
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
        return null;
    }

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

    public ArrayList<String> makeRevercePMAP(PolicyMap PMAP) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        commands.add("no policy-map " + PMAP.getName());
        commands.add("interface " + LAN.snapshotList.get(LAN.snapshotList.size() - 1).getIfDescr());
        commands.add("no service-policy output " + PMAP.getName());
        commands.add("exit");
        commands.add("exit");
        return commands;
    }

    public ArrayList<String> makeReverceCMAP(ClassMap CMAP) {
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        commands.add("no class-map " + CMAP.getName());
        commands.add("exit");
        return commands;
    }
}
