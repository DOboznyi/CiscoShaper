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
}
