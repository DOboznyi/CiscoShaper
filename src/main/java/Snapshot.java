public class Snapshot {
    String ifDescr;
    int ifType;
    int ifMtu;
    long ifSpeed;
    String ifPhysAddress;
    int ifAdminStatus;
    int ifOperStatus;
    String ifLastChange;
    int ifInOctets;
    int ifInUcastPkts;
    int ifInNUcastPkts;
    int ifInDiscards;
    int ifInErrors;
    int ifInUnknownProtos;
    int ifOutOctets;
    int ifOutUcastPkts;
    int ifOutNUcastPkts;
    int ifOutDiscards;
    int ifOutErrors;
    int ifOutQLen;
    int ifSpecific;
    String ifAlias;
    long Time;
    long realSpeed;

    public long getRealSpeed() {
        return realSpeed;
    }

    public void setRealSpeed(long realSpeed) {
        this.realSpeed = realSpeed;
    }

    public String getIfAlias() {
        return ifAlias;
    }

    public void setIfAlias(String ifAlias) {
        this.ifAlias = ifAlias;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public int getIfType() {
        return ifType;
    }

    public void setIfType(int ifType) {
        this.ifType = ifType;
    }

    public int getIfMtu() {
        return ifMtu;
    }

    public void setIfMtu(int ifMtu) {
        this.ifMtu = ifMtu;
    }

    public long getIfSpeed() {
        return ifSpeed;
    }

    public void setIfSpeed(long ifSpeed) {
        this.ifSpeed = ifSpeed;
    }

    public String getIfPhysAddress() {
        return ifPhysAddress;
    }

    public void setIfPhysAddress(String ifPhysAddress) {
        this.ifPhysAddress = ifPhysAddress;
    }

    public int getIfAdminStatus() {
        return ifAdminStatus;
    }

    public void setIfAdminStatus(int ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    public int getIfOperStatus() {
        return ifOperStatus;
    }

    public void setIfOperStatus(int ifOperStatus) {
        this.ifOperStatus = ifOperStatus;
    }

    public String getIfLastChange() {
        return ifLastChange;
    }

    public void setIfLastChange(String ifLastChange) {
        this.ifLastChange = ifLastChange;
    }

    public int getIfInOctets() {
        return ifInOctets;
    }

    public void setIfInOctets(int ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    public int getIfInUcastPkts() {
        return ifInUcastPkts;
    }

    public void setIfInUcastPkts(int ifInUcastPkts) {
        this.ifInUcastPkts = ifInUcastPkts;
    }

    public int getIfInNUcastPkts() {
        return ifInNUcastPkts;
    }

    public void setIfInNUcastPkts(int ifInNUcastPkts) {
        this.ifInNUcastPkts = ifInNUcastPkts;
    }

    public int getIfInDiscards() {
        return ifInDiscards;
    }

    public void setIfInDiscards(int ifInDiscards) {
        this.ifInDiscards = ifInDiscards;
    }

    public int getIfInErrors() {
        return ifInErrors;
    }

    public void setIfInErrors(int ifInErrors) {
        this.ifInErrors = ifInErrors;
    }

    public int getIfInUnknownProtos() {
        return ifInUnknownProtos;
    }

    public void setIfInUnknownProtos(int ifInUnknownProtos) {
        this.ifInUnknownProtos = ifInUnknownProtos;
    }

    public int getIfOutOctets() {
        return ifOutOctets;
    }

    public void setIfOutOctets(int ifOutOctets) {
        this.ifOutOctets = ifOutOctets;
    }

    public int getIfOutUcastPkts() {
        return ifOutUcastPkts;
    }

    public void setIfOutUcastPkts(int ifOutUcastPkts) {
        this.ifOutUcastPkts = ifOutUcastPkts;
    }

    public int getIfOutNUcastPkts() {
        return ifOutNUcastPkts;
    }

    public void setIfOutNUcastPkts(int ifOutNUcastPkts) {
        this.ifOutNUcastPkts = ifOutNUcastPkts;
    }

    public int getIfOutDiscards() {
        return ifOutDiscards;
    }

    public void setIfOutDiscards(int ifOutDiscards) {
        this.ifOutDiscards = ifOutDiscards;
    }

    public int getIfOutErrors() {
        return ifOutErrors;
    }

    public void setIfOutErrors(int ifOutErrors) {
        this.ifOutErrors = ifOutErrors;
    }

    public int getIfOutQLen() {
        return ifOutQLen;
    }

    public void setIfOutQLen(int ifOutQLen) {
        this.ifOutQLen = ifOutQLen;
    }

    public int getIfSpecific() {
        return ifSpecific;
    }

    public void setIfSpecific(int ifSpecific) {
        this.ifSpecific = ifSpecific;
    }

    public void addInfo(String info, int Column, long Time){
        this.Time = Time;
        switch (Column){
            case 2: setIfDescr(info);
                break;
            case 3: setIfType(Integer.parseInt(info));
                break;
            case 4: setIfMtu(Integer.parseInt(info));
                break;
            case 5: setIfSpeed(Long.parseLong(info));
                break;
            case 6: setIfPhysAddress(info);
                break;
            case 7: setIfAdminStatus(Integer.parseInt(info));
                break;
            case 8: setIfOperStatus(Integer.parseInt(info));
                break;
            case 9: setIfLastChange(info);
                break;
            case 10: setIfInOctets(Integer.parseInt(info));
                break;
            case 11: setIfInUcastPkts(Integer.parseInt(info));
                break;
            case 12: setIfInNUcastPkts(Integer.parseInt(info));
                break;
            case 13: setIfInDiscards(Integer.parseInt(info));
                break;
            case 14: setIfInErrors(Integer.parseInt(info));
                break;
            case 15: setIfInUnknownProtos(Integer.parseInt(info));
                break;
            case 16: setIfOutOctets(Integer.parseInt(info));
                break;
            case 17: setIfOutUcastPkts(Integer.parseInt(info));
                break;
            case 18: setIfOutNUcastPkts(Integer.parseInt(info));
                break;
            case 19: setIfOutDiscards(Integer.parseInt(info));
                break;
            case 20: setIfOutErrors(Integer.parseInt(info));
                break;
            case 21: setIfOutQLen(Integer.parseInt(info));
                break;
            case 22: setIfSpecific(Integer.parseInt(info));
                break;
        }
    }
}
