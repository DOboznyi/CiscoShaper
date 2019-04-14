import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

/**
 * Class for describing SNMP client.
 */
public class SNMPClient
{

    private static Logger logger = Logger.getLogger(SNMPClient.class.getName());


    Snmp snmp;
    /**
     * IP address with port to get info via SNMP.
     */
    String address;
    /**
     * Community string for connecting via SNMP.
     */
    private String community;
    /**
     * Version of SNMP protocol.
     */
    private String version;
    //v3 support
    /**
     * Username for connecting via SNMP V3.
     */
    private String username;
    /**
     * Password for connecting via SNMP V3.
     */
    private String password;
    /**
     * Authentification protocol for connecting via SNMP V3.
     */
    private String authprotocol;
    /**
     * Privacy passphrase for encryption.
     */
    private String privacypassphrase;
    /**
     * Privacy protocol for connecting via SNMP V3.
     */
    private String privacyprotocol;
    /**
     * Context for connecting via SNMP V3.
     */
    private String context;

    /**
     * Class to implement basic object in SNMP
     */
    public static class SNMPTriple
    {
        /**
         * OID of object
         */
        public String oid;
        /**
         * Name of object
         */
        public String name;
        /**
         * Value which given via SNMP
         */
        public String value;

        /**
         * Constructor for basic object in SNMP
         * @param oid OID of object
         * @param name name of object
         * @param value value which given via SNMP
         */
        public SNMPTriple(String oid, String name, String value)
        {
            this.oid = oid;
            this.name = name;
            this.value = value;
        }
    }

    /**
     * Constructor for SNMP client.
     * @param host_name name of the host. IP address or domain name.
     * @param community community string for connecting via SNMP.
     * @param port port which use host to communicate via SNMP.
     */
    public SNMPClient(String host_name, String community, String port)
    {
        this.community = community;
        address = "udp:"+host_name+"/"+port;
    }

    /**
     * Method to start SNMP connection.
     * @throws IOException when connection was failed.
     */
    public void start() throws IOException
    {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        if("3".equals(this.version))//add v3 support
        {
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
        }
        // Do not forget this line!
        transport.listen();
    }

    /**
     * Method to start SNMP connection.
     * @throws IOException when connection was failed.
     */
    public void stop() throws IOException
    {
        if(snmp!=null)snmp.close();
        snmp = null;
    }

    /**
     * Method which takes a single OID and returns the response from the agent as a String.
     * @param oid OID of object.
     * @return string with response.
     * @throws IOException when response was failed.
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent res = getEvent(new OID[] { oid });
        if(res!=null)
            return res.getResponse().get(0).getVariable().toString();
        return null;
    }

    /**
     * Method to create PDU for SNMP connection.
     * @return PDU for some version of SNMP.
     */
    private PDU createPDU() {
        if(!"3".equals(this.version))
            return new PDU();
        ScopedPDU pdu = new ScopedPDU();
        if(this.context != null)
            pdu.setContextEngineID(new OctetString(this.context));    //if not set, will be SNMP engine id
        return pdu;
    }

    /**
     * This method is capable of handling multiple OIDs.
     * @param oids list of OIDs of object.
     * @return map with responses.
     * @throws IOException when connection timed out.
     */
    public Map<OID, String> get(OID oids[]) throws IOException
    {
        PDU pdu = createPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if(event != null) {
            PDU pdu2 = event.getResponse();
            VariableBinding[] binds = pdu2!=null?event.getResponse().toArray():null;
            if(binds!=null)
            {
                Map<OID, String> res = new LinkedHashMap<OID, String>(binds.length);
                for(VariableBinding b: binds)
                    res.put(b.getOid(), b.getVariable().toString());
                return res;
            }else return null;
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * Method to make response event via SNMP.
     * @param oids OIDS for making response event.
     * @return response event.
     * @throws IOException when response timed out.
     */
    public ResponseEvent getEvent(OID oids[]) throws IOException
    {
        PDU pdu = createPDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if(event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * This method returns a Target, which contains information about
     * where the data should be fetched and how.
     * @return target with all information about connection.
     */
    private Target getTarget() {
        if("3".equals(this.version))return getTargetV3();
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        //logger.info("snmp version "+this.version+", community: "+this.community);
        if(this.community == null)
            target.setCommunity(new OctetString("public"));
        else
            target.setCommunity(new OctetString(this.community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(5000);
        target.setVersion(this.getVersionInt());
        return target;
    }

    /**
     * This method returns a Target for SNMP V3, which contains information about
     * where the data should be fetched and how.
     * @return target with all information about connection via SNMP V3.
     */
    private Target getTargetV3() {
        //logger.info("Use SNMP v3, "+this.privacyprotocol +"="+this.password+", "+this.privacyprotocol+"="+this.privacypassphrase);
        OID authOID = AuthMD5.ID;
        if("SHA".equals(this.authprotocol))
            authOID = AuthSHA.ID;
        OID privOID = PrivDES.ID;
        if(this.privacyprotocol == null)
            privOID = null;
        UsmUser user = new UsmUser(new OctetString(this.username),
                authOID, new OctetString(this.password),  //auth
                privOID, this.privacypassphrase!=null?new OctetString(this.privacypassphrase):null); //enc
        snmp.getUSM().addUser(new OctetString(this.username), user);
        Address targetAddress = GenericAddress.parse(address);
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(this.getVersionInt());
        if(privOID != null)
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        else
            target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
        target.setSecurityName(new OctetString(this.username));
        return target;
    }

    /**
     * Method to get table of interfaces on host.
     * @param oid OID for get table.
     * @return hash map with indexes and ports.
     * @throws IOException when connection was failed.
     */
    public HashMap<Integer,Port> getInterfaceTable(String oid) throws IOException
    {
        if(oid == null)return null;
        if(!oid.startsWith("."))oid = "."+oid;
        TableUtils tUtils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = tUtils.getTable(getTarget(), new OID[]{new OID(oid)}, null, null);

        HashMap<Integer,Port> portList = new HashMap<Integer,Port>();
        long time = System.currentTimeMillis();
        for (TableEvent event : events) {
            if(event.isError()) {
                logger.warning(this.address + ": SNMP event error: "+event.getErrorMessage());
                continue;
                //throw new RuntimeException(event.getErrorMessage());
            }
            for(VariableBinding vb: event.getColumns()) {
                String key = "."+vb.getOid().toString();
                String newKey = key.replace(oid+".1.","");
                String[] tokens = newKey.split("\\.");
                int column = Integer.parseInt(tokens[0]);
                int port = Integer.parseInt(tokens[1]);
                String value = vb.getVariable().toString();
                if (portList.get(port)==null)
                    portList.put(port,new Port(port));
                portList.get(port).addInfo(value, column,time);
            }
        }
        for (Map.Entry<Integer,Port> entry : portList.entrySet()) {
            String response = getAsString(new OID(".1.3.6.1.2.1.31.1.1.1.18."+entry.getValue().getIfIndex()));
            if (!response.equals("")) {
                String[] tempArray = response.split(" ");
                entry.getValue().snapshotList.get(entry.getValue().snapshotList.size() - 1).setIfAlias(tempArray[0]);
                entry.getValue().snapshotList.get(entry.getValue().snapshotList.size() - 1).setRealSpeed(Long.parseLong(tempArray[1]));
            }
        }
        return portList;
    }

    /**
     * Update network usage on port.
     * @param Port object which contain all information about port.
     * @param oid OID for updating information.
     */
    public void updatePort (Port Port, String oid){
        if(oid == null)return;
        if(!oid.startsWith("."))oid = "."+oid;
        TableUtils tUtils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = tUtils.getTable(getTarget(), new OID[]{new OID(oid)}, null, null);

        long time = System.currentTimeMillis();
        for (TableEvent event : events) {
            if(event.isError()) {
                logger.warning(this.address + ": SNMP event error: "+event.getErrorMessage());
                continue;
                //throw new RuntimeException(event.getErrorMessage());
            }
            for(VariableBinding vb: event.getColumns()) {
                String key = "."+vb.getOid().toString();
                String newKey = key.replace(oid+".1.","");
                String[] tokens = newKey.split("\\.");
                int column = Integer.parseInt(tokens[0]);
                int port = Integer.parseInt(tokens[1]);
                String value = vb.getVariable().toString();
                if (port == Port.getIfIndex()) Port.addInfo(value, column,time);
            }
        }
    }

    /**
     * Method for updating NBAR information.
     * @param oid OID for update.
     * @param portList list of ports.
     * @throws IOException when update was failed.
     */
    public void updateTrafficTable(String oid, HashMap<Integer,Port> portList) throws IOException
    {
        if(oid == null)return;
        if(!oid.startsWith("."))oid = "."+oid;
        TableUtils tUtils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = tUtils.getTable(getTarget(), new OID[]{new OID(oid)}, null, null);

        //ArrayList<Traffic> TrafficList = new ArrayList<Port>();
        long time = System.currentTimeMillis();
        for (TableEvent event : events) {
            if(event.isError()) {
                logger.warning(this.address + ": SNMP event error: "+event.getErrorMessage());
                continue;
                //throw new RuntimeException(event.getErrorMessage());
            }
            for(VariableBinding vb: event.getColumns()) {
                String key = "."+vb.getOid().toString();
                String newKey = key.replace(oid+".1.","");
                String[] tokens = newKey.split("\\.");
                int column = Integer.parseInt(tokens[0]);
                int port = Integer.parseInt(tokens[1]);
                int index = Integer.parseInt(tokens[2]);
                if (portList.get(port).getTimestampList().size()==0||portList.get(port).getTimestampList().get(portList.get(port).getTimestampList().size()-1).Time!=time) portList.get(port).getTimestampList().add(new Timestamp(time));
                String value = vb.getVariable().toString();
                portList.get(port).getTimestampList().get(portList.get(port).getTimestampList().size()-1).addInfo(value,column,index);
                //portList.add(new SNMPTriple(key, "", value));
            }
        }
    }

    /**
     * Method to get object with information about version of SNMP.
     * @return version of SNMP.
     */
    public int getVersionInt()
    {
        if("1".equals(this.version))
            return SnmpConstants.version1;
        else if("3".equals(this.version))
            return SnmpConstants.version3;
        else
            return SnmpConstants.version2c;
    }
}
