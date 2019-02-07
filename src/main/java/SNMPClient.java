/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */

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
 * A simple SNMP client to poll a single MySQL server for OS performance metrics, such as CPU, memory, disk usage, etc.
 * This SNMP client uses snmp4j open source library.
 * @author xrao
 *
 */
public class SNMPClient
{
    //common UC DAVIS oid
    public final static String memTotalSwap =      ".1.3.6.1.4.1.2021.4.3.0";
    public final static String memAvailSwap =      ".1.3.6.1.4.1.2021.4.4.0";
    public final static String memTotalReal =      ".1.3.6.1.4.1.2021.4.5.0";
    public final static String memAvailReal =      ".1.3.6.1.4.1.2021.4.6.0";
    public final static String memTotalSwapTXT =   ".1.3.6.1.4.1.2021.4.7.0";
    public final static String memTotalRealTXT =   ".1.3.6.1.4.1.2021.4.9.0";
    public final static String memTotalFree =      ".1.3.6.1.4.1.2021.4.11.0";
    public final static String memShared =         ".1.3.6.1.4.1.2021.4.13.0";
    public final static String memBuffer =         ".1.3.6.1.4.1.2021.4.14.0";
    public final static String memCached =         ".1.3.6.1.4.1.2021.4.15.0";
    public final static String memUsedSwapTXT =    ".1.3.6.1.4.1.2021.4.16.0";
    public final static String memUsedRealTXT =    ".1.3.6.1.4.1.2021.4.17.0";
    public final static String ssSwapIn =          ".1.3.6.1.4.1.2021.11.3.0";
    public final static String ssSwapOut =         ".1.3.6.1.4.1.2021.11.4.0";
    public final static String ssIOSent =          ".1.3.6.1.4.1.2021.11.5.0";
    public final static String ssIOReceive =       ".1.3.6.1.4.1.2021.11.6.0";
    public final static String ssSysInterrupts =   ".1.3.6.1.4.1.2021.11.7.0";
    public final static String ssSysContext =      ".1.3.6.1.4.1.2021.11.8.0";
    public final static String ssCpuUser =         ".1.3.6.1.4.1.2021.11.9.0";
    public final static String ssCpuSystem =       ".1.3.6.1.4.1.2021.11.10.0";
    public final static String ssCpuIdle =         ".1.3.6.1.4.1.2021.11.11.0";
    public final static String ssCpuRawUser =      ".1.3.6.1.4.1.2021.11.50.0";
    public final static String ssCpuRawNice =      ".1.3.6.1.4.1.2021.11.51.0";
    public final static String ssCpuRawSystem=     ".1.3.6.1.4.1.2021.11.52.0";
    public final static String ssCpuRawIdle =      ".1.3.6.1.4.1.2021.11.53.0";
    public final static String ssCpuRawWait =      ".1.3.6.1.4.1.2021.11.54.0";
    public final static String ssCpuRawKernel =    ".1.3.6.1.4.1.2021.11.55.0";
    public final static String ssCpuRawInterrupt = ".1.3.6.1.4.1.2021.11.56.0";
    public final static String ssIORawSent =       ".1.3.6.1.4.1.2021.11.57.0";
    public final static String ssIORawReceived =   ".1.3.6.1.4.1.2021.11.58.0";
    public final static String ssRawInterrupts =   ".1.3.6.1.4.1.2021.11.59.0";
    public final static String ssRawContexts =     ".1.3.6.1.4.1.2021.11.60.0";
    public final static String ssCpuRawSoftIRQ =   ".1.3.6.1.4.1.2021.11.61.0";
    public final static String ssRawSwapIn =       ".1.3.6.1.4.1.2021.11.62.0";
    public final static String ssRawSwapOut =      ".1.3.6.1.4.1.2021.11.63.0";
    public final static String ssCpuRawSteal =      ".1.3.6.1.4.1.2021.11.64.0";
    public final static String ssCpuRawGuest =      ".1.3.6.1.4.1.2021.11.65.0";
    public final static String ssCpuRawGuestNice =  ".1.3.6.1.4.1.2021.11.66.0";
    public final static String laLoad1m =          ".1.3.6.1.4.1.2021.10.1.3.1";
    public final static String laLoad5m =          ".1.3.6.1.4.1.2021.10.1.3.2";
    public final static String laLoad15m =         ".1.3.6.1.4.1.2021.10.1.3.3";
    public final static String hrSystemUptime =    ".1.3.6.1.2.1.25.1.1.0";
    public final static String hrSystemNumUsers=   ".1.3.6.1.2.1.25.1.5.0";
    public final static String hrSystemProcesses = ".1.3.6.1.2.1.25.1.6.0";
    public final static String tcpAttemptFails =   ".1.3.6.1.2.1.6.7.0";
    public final static String tcpCurrEstab =      ".1.3.6.1.2.1.6.9.0";
    //tcpCurrEstab
    public static OID[] COMMON_SYS_OIDS = null;

    public static final  Map<String, String> OID_MAP = new LinkedHashMap<String, String>(); //Name to OID
    public static final Map<String, String> OID_NAME_MAP = new LinkedHashMap<String, String>(); //OID to Name
    private static Logger logger = Logger.getLogger(SNMPClient.class.getName());

    Snmp snmp = null;
    String address = null;
    private String community;
    private String version;
    //v3 support
    private String username;
    private String password;
    private String authprotocol;
    private String privacypassphrase;
    private String privacyprotocol;
    private String context;
    static
    {
        OID_MAP.put("memTotalSwap",memTotalSwap);
        OID_MAP.put("memAvailSwap", memAvailSwap);
        OID_MAP.put("memTotalReal", memTotalReal);
        OID_MAP.put("memAvailReal", memAvailReal);
        OID_MAP.put("memTotalSwapTXT", memTotalSwapTXT);
        OID_MAP.put("memTotalRealTXT", memTotalRealTXT);
        OID_MAP.put("memTotalFree", memTotalFree);
        OID_MAP.put("memShared", memShared);
        OID_MAP.put("memBuffer",    memBuffer);
        OID_MAP.put("memCached",    memCached);
        OID_MAP.put("memUsedSwapTXT", memUsedSwapTXT);
        OID_MAP.put("memUsedRealTXT", memUsedRealTXT);
        OID_MAP.put("ssSwapIn",     ssSwapIn);
        OID_MAP.put("ssSwapOut",    ssSwapOut);
        OID_MAP.put("ssIOSent",     ssIOSent);
        OID_MAP.put("ssIOReceive",  ssIOReceive);
        OID_MAP.put("ssSysInterrupts",ssSysInterrupts);
        OID_MAP.put("ssSysContext",  ssSysContext);
        OID_MAP.put("ssCpuUser",     ssCpuUser);
        OID_MAP.put("ssCpuSystem",   ssCpuSystem);
        OID_MAP.put("ssCpuIdle",     ssCpuIdle);
        OID_MAP.put("ssCpuRawUser",  ssCpuRawUser);
        OID_MAP.put("ssCpuRawNice",  ssCpuRawNice);
        OID_MAP.put("ssCpuRawSystem",ssCpuRawSystem);
        OID_MAP.put("ssCpuRawIdle",  ssCpuRawIdle);
        OID_MAP.put("ssCpuRawWait",  ssCpuRawWait);
        OID_MAP.put("ssCpuRawKernel", ssCpuRawKernel);
        OID_MAP.put("ssCpuRawInterrupt",ssCpuRawInterrupt);
        OID_MAP.put("ssIORawSent",     ssIORawSent);
        OID_MAP.put("ssIORawReceived" ,ssIORawReceived);
        OID_MAP.put("ssRawInterrupts", ssRawInterrupts);
        OID_MAP.put("ssRawContexts",   ssRawContexts);
        OID_MAP.put("ssCpuRawSoftIRQ", ssCpuRawSoftIRQ);
        OID_MAP.put("ssRawSwapIn",     ssRawSwapIn);
        OID_MAP.put("ssRawSwapOut",    ssRawSwapOut);
        OID_MAP.put("ssCpuRawSteal",    ssCpuRawSteal);
        OID_MAP.put("ssCpuRawGuest",    ssCpuRawGuest);
        OID_MAP.put("ssCpuRawGuestNice",    ssCpuRawGuestNice);
        OID_MAP.put("laLoad1m",  laLoad1m);
        OID_MAP.put("laLoad5m",  laLoad5m);
        OID_MAP.put("laLoad15m",  laLoad15m);
        OID_MAP.put("hrSystemUptime",hrSystemUptime);
        OID_MAP.put("hrSystemNumUsers",hrSystemNumUsers);
        OID_MAP.put("hrSystemProcesses",hrSystemProcesses);
        OID_MAP.put("tcpAttemptFails",tcpAttemptFails);
        OID_MAP.put("tcpCurrEstab",tcpCurrEstab);

        COMMON_SYS_OIDS = new OID[OID_MAP.size()];
        int oidIdx = 0;
        for(Map.Entry<String, String> e: OID_MAP.entrySet())
        {
            OID_NAME_MAP.put(e.getValue(), e.getKey());
            COMMON_SYS_OIDS[oidIdx] = new OID(e.getValue());
            oidIdx ++;
        }
    };

    public static class SNMPTriple
    {
        public String oid;
        public String name;
        public String value;

        public SNMPTriple(String oid, String name, String value)
        {
            this.oid = oid;
            this.name = name;
            this.value = value;
        }
    }

    public SNMPClient(String host_name, String community, String port)
    {
        this.community = community;
        address = "udp:"+host_name+"/"+port;
    }

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

    public void stop() throws IOException
    {
        if(snmp!=null)snmp.close();
        snmp = null;
    }
    /**
     * Method which takes a single OID and returns the response from the agent as a String.
     * @param oid
     * @return
     * @throws IOException
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent res = getEvent(new OID[] { oid });
        if(res!=null)
            return res.getResponse().get(0).getVariable().toString();
        return null;
    }

    private PDU createPDU() {
        if(!"3".equals(this.version))
            return new PDU();
        ScopedPDU pdu = new ScopedPDU();
        if(this.context != null)
            pdu.setContextEngineID(new OctetString(this.context));    //if not set, will be SNMP engine id
        return pdu;
    }

    /**
     * This method is capable of handling multiple OIDs
     * @param oids
     * @return
     * @throws IOException
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
     * @return
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

    public List<SNMPTriple> querySingleSNMPEntryByOID(String oid) throws IOException
    {
        if(oid == null)return null;
        if(!oid.startsWith("."))oid = "."+oid;
        List<SNMPTriple> snmpList = new ArrayList<SNMPTriple>();
        Map<OID, String> res = get(new OID[]{new OID(oid)});
        if(res!=null)
        {
            for(Map.Entry<OID, String> e: res.entrySet())
            {
                //if("noSuchObject".equalsIgnoreCase(e.getValue()))continue;
                snmpList.add(new SNMPTriple(e.getKey().toString(), "", e.getValue()));
            }
        }
        return snmpList;
    }

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

    public List<SNMPTriple> querySingleSNMPTableByOID(String oid) throws IOException
    {
        if(oid == null)return null;
        if(!oid.startsWith("."))oid = "."+oid;
        TableUtils tUtils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = tUtils.getTable(getTarget(), new OID[]{new OID(oid)}, null, null);

        List<SNMPTriple> snmpList = new ArrayList<SNMPTriple>();

        for (TableEvent event : events) {
            if(event.isError()) {
                logger.warning(this.address + ": SNMP event error: "+event.getErrorMessage());
                continue;
                //throw new RuntimeException(event.getErrorMessage());
            }
            for(VariableBinding vb: event.getColumns()) {
                String key = vb.getOid().toString();
                String value = vb.getVariable().toString();
                snmpList.add(new SNMPTriple(key, "", value));
            }
        }
        return snmpList;
    }


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
