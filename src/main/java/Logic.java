import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Logic {
    Port WAN;
    Long bandwidth;
    Port LAN;
    double percent;
    double allowedPercent;
    ArrayList<String> allowed;
    ArrayList<PolicyMap> policyMaps;
    int shaper;
    Host host;

    public Logic(Host host,Port WAN, Port LAN, double percent, double allowedPercent, ArrayList<String> allowed){
        this.host = host;
        this.WAN = WAN;
        this.LAN = LAN;
        this.percent = percent;
        this.allowed = allowed;
        this.allowedPercent = allowedPercent;
        SshClient ssh = new SshClient(host.getUser(),host.getName(),host.getPassword());
        ArrayList<String> result = ssh.executeCommand("show running-config interface "+WAN.snapshotList.get(WAN.snapshotList.size()-1).getIfDescr());
        for (String str: result) {
            if (str.contains("bandwidth qos-reference")) {
                bandwidth = Long.parseLong(str.replaceAll(str, ""));
                break;
            }
        }
    }

    private void watch(){
        HashMap<Integer, Traffic> lastDump = WAN.getTimestampList().get(WAN.getTimestampList().size()-1).getDump();
        HashMap<Integer, Traffic> preLastDump = WAN.getTimestampList().get(WAN.getTimestampList().size()-2).getDump();
        SNMPClient snmp = new SNMPClient(host.getName(),host.getCommunity(),"161");
        snmp.updatePort(WAN,".1.3.6.1.2.1.2.2");
        long lastUsageTime = WAN.snapshotList.get(WAN.snapshotList.size()-1).Time ;
        long preLastUsageTime = WAN.snapshotList.get(WAN.snapshotList.size()-2).Time;
        long lastUsageOctets = WAN.snapshotList.get(WAN.snapshotList.size()-1).getIfInOctets();
        long preLastUsageOctets = WAN.snapshotList.get(WAN.snapshotList.size()-2).getIfInOctets();
        long currentBandwidthUsage = (lastUsageOctets - preLastUsageOctets)/(lastUsageTime-preLastUsageTime);
        if ( currentBandwidthUsage/bandwidth>percent ) {
            HashMap<Integer, Long> delta = new HashMap<Integer, Long>();
            long maxDelta = 0;
            String deltaName = "";
            long deltaSum = 0;
            long maxAllowed = 0;
            long allowedSum = 0;
            for (HashMap.Entry<Integer, Traffic> entry : lastDump.entrySet()) {
                int index = entry.getKey();
                if (preLastDump.get(index)==null) delta.put(index, entry.getValue().getCnpdAllStatsInBytes());
                else delta.put(index, entry.getValue().getCnpdAllStatsInBytes() - preLastDump.get(index).getCnpdAllStatsInBytes());
                if (maxDelta<delta.get(index)){
                    if (!allowed.contains(lastDump.get(index).getCnpdAllStatsProtocolsName())) {
                        maxDelta = delta.get(index);
                        deltaSum += maxDelta;
                        deltaName = lastDump.get(index).getCnpdAllStatsProtocolsName();
                    }
                    else {
                        maxAllowed = delta.get(index);
                        allowedSum += delta.get(index);
                    }
                }
            }
            //Надо найти сумму разрешенных и неразрешенных октетов и тогда уже блочить
            if (maxDelta!=0){
                if (allowedSum/(deltaSum+allowedSum)<allowedPercent){
                    makeShaping(deltaName, 300);
                }
            }
        }
        checkMaps();
    }

    private void makeShaping(String deltaName, int ttl){
        String Time = getCurrentTimeUsingDate();
        ClassMap CMAP = new ClassMap("match-all","CMAP"+Time,deltaName);
        PolicyMap PMAP = new PolicyMap("PMAP"+Time,CMAP,shaper);
        SshClient ssh = new SshClient(host.getUser(),host.getName(),host.getPassword());
        try
        {
            ssh.RunCommands(makeCommand(PMAP));
        }
        catch (Exception ee){
            System.out.println(ee);
        }
        PMAP.setReverceCommands(makeReverce(PMAP));
        PMAP.setTTL(ttl);
        policyMaps.add(PMAP);
    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "yyyyMMddHHmmss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }

    public Date getCurrentDate() {
        Date date = new Date();
        return date;
    }

    public ArrayList<String> makeCommand(PolicyMap PMAP){
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        ClassMap CMAP = PMAP.getClassMap();
        commands.add("class-map "+CMAP.getMatch()+" "+CMAP.getName());
        commands.add("match protocol "+CMAP.getProtocol());
        commands.add("exit");
        commands.add("policy-map "+PMAP.getName());
        commands.add("class " + CMAP.getName());
        commands.add("police "+PMAP.getShaper()+" conform-action transmit exceed-action drop");
        commands.add("exit");
        commands.add("exit");
        commands.add("interface "+LAN.snapshotList.get(LAN.snapshotList.size()-1).getIfDescr());
        commands.add("service-policy output "+PMAP.getName());
        commands.add("exit");
        commands.add("exit");
        return commands;
    }

    public ArrayList<String> makeReverce(PolicyMap PMAP){
        ArrayList<String> commands = new ArrayList<String>();
        commands.add("configure terminal");
        ClassMap CMAP = PMAP.getClassMap();
        commands.add("no class-map "+CMAP.getMatch()+" "+CMAP.getName());
        commands.add("no policy-map "+PMAP.getName());
        commands.add("interface "+LAN.snapshotList.get(LAN.snapshotList.size()-1).getIfDescr());
        commands.add("no service-policy output "+PMAP.getName());
        commands.add("exit");
        commands.add("exit");
        return commands;
    }

    public void checkMaps(){
        Iterator<PolicyMap> p = policyMaps.iterator();
        while(p.hasNext()){
            PolicyMap PMAP = p.next();
            String name = PMAP.getName();
            Date Time = stringToDate(name.replace("PMAP",""));
            Calendar cal = Calendar.getInstance();
            cal.setTime(Time);
            cal.add(Calendar.SECOND, 300);
            Date newTime = cal.getTime();
            if (getCurrentDate().after(newTime)){
                SshClient ssh = new SshClient(host.getUser(),host.getName(),host.getPassword());
                try
                {
                    ssh.RunCommands(PMAP.getReverceCommands());
                }
                catch (Exception ee){
                    System.out.println(ee);
                }
                p.remove();
            }
        }
    }

    public Date stringToDate(String Time){
        Date date = new Date();
        String strDateFormat = "yyyyMMddHHmmss";
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try
        {
            date = format.parse(Time);
        }
        catch (Exception ee){
            System.out.println(ee);
        }
        return date;
    }
}
