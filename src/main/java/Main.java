import org.snmp4j.smi.OID;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Hello World!");
        SNMPClient snmpClient = new SNMPClient("172.16.0.131", "public","161");
        snmpClient.start();
        HashMap<Integer,Port> ports =snmpClient.getInterfaceTable(".1.3.6.1.2.1.2.2");
        snmpClient.updateTrafficTable(".1.3.6.1.4.1.9.9.244.1.2.1",ports);
        String res = snmpClient.getAsString(new OID(".1.3.6.1.2.1.31.1.1.1.18.2"));
    }
}