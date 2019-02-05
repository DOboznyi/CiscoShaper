public class Traffic {
    int cnpdAllStatsProtocolsIndex;
    String CnpdAllStatsProtocolsName;
    long cnpdAllStatsInPkts;
    long cnpdAllStatsOutPkts;
    long cnpdAllStatsInBytes;
    long cnpdAllStatsOutBytes;
    long cnpdAllStatsHCInPkts;
    long cnpdAllStatsHCOutPkts;
    long cnpdAllStatsHCInBytes;
    long cnpdAllStatsHCOutBytes;
    long cnpdAllStatsInBitRate;
    long cnpdAllStatsOutBitRate;

    long Time;

    public Traffic(int cnpdAllStatsProtocolsIndex) {
        this.cnpdAllStatsProtocolsIndex = cnpdAllStatsProtocolsIndex;
    }

    public int getCnpdAllStatsProtocolsIndex() {
        return cnpdAllStatsProtocolsIndex;
    }

    public void setCnpdAllStatsProtocolsIndex(int cnpdAllStatsProtocolsIndex) {
        this.cnpdAllStatsProtocolsIndex = cnpdAllStatsProtocolsIndex;
    }

    public String getCnpdAllStatsProtocolsName() {
        return CnpdAllStatsProtocolsName;
    }

    public void setCnpdAllStatsProtocolsName(String cnpdAllStatsProtocolsName) {
        CnpdAllStatsProtocolsName = cnpdAllStatsProtocolsName;
    }

    public long getCnpdAllStatsInPkts() {
        return cnpdAllStatsInPkts;
    }

    public void setCnpdAllStatsInPkts(long cnpdAllStatsInPkts) {
        this.cnpdAllStatsInPkts = cnpdAllStatsInPkts;
    }

    public long getCnpdAllStatsOutPkts() {
        return cnpdAllStatsOutPkts;
    }

    public void setCnpdAllStatsOutPkts(long cnpdAllStatsOutPkts) {
        this.cnpdAllStatsOutPkts = cnpdAllStatsOutPkts;
    }

    public long getCnpdAllStatsInBytes() {
        return cnpdAllStatsInBytes;
    }

    public void setCnpdAllStatsInBytes(long cnpdAllStatsInBytes) {
        this.cnpdAllStatsInBytes = cnpdAllStatsInBytes;
    }

    public long getCnpdAllStatsOutBytes() {
        return cnpdAllStatsOutBytes;
    }

    public void setCnpdAllStatsOutBytes(long cnpdAllStatsOutBytes) {
        this.cnpdAllStatsOutBytes = cnpdAllStatsOutBytes;
    }

    public long getCnpdAllStatsHCInPkts() {
        return cnpdAllStatsHCInPkts;
    }

    public void setCnpdAllStatsHCInPkts(long cnpdAllStatsHCInPkts) {
        this.cnpdAllStatsHCInPkts = cnpdAllStatsHCInPkts;
    }

    public long getCnpdAllStatsHCOutPkts() {
        return cnpdAllStatsHCOutPkts;
    }

    public void setCnpdAllStatsHCOutPkts(long cnpdAllStatsHCOutPkts) {
        this.cnpdAllStatsHCOutPkts = cnpdAllStatsHCOutPkts;
    }

    public long getCnpdAllStatsHCInBytes() {
        return cnpdAllStatsHCInBytes;
    }

    public void setCnpdAllStatsHCInBytes(long cnpdAllStatsHCInBytes) {
        this.cnpdAllStatsHCInBytes = cnpdAllStatsHCInBytes;
    }

    public long getCnpdAllStatsHCOutBytes() {
        return cnpdAllStatsHCOutBytes;
    }

    public void setCnpdAllStatsHCOutBytes(long cnpdAllStatsHCOutBytes) {
        this.cnpdAllStatsHCOutBytes = cnpdAllStatsHCOutBytes;
    }

    public long getCnpdAllStatsInBitRate() {
        return cnpdAllStatsInBitRate;
    }

    public void setCnpdAllStatsInBitRate(long cnpdAllStatsInBitRate) {
        this.cnpdAllStatsInBitRate = cnpdAllStatsInBitRate;
    }

    public long getCnpdAllStatsOutBitRate() {
        return cnpdAllStatsOutBitRate;
    }

    public void setCnpdAllStatsOutBitRate(long cnpdAllStatsOutBitRate) {
        this.cnpdAllStatsOutBitRate = cnpdAllStatsOutBitRate;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public void addInfo(String info, int Column){
        switch (Column){
            case 1: setCnpdAllStatsProtocolsIndex(Integer.parseInt(info));
                break;
            case 2: setCnpdAllStatsProtocolsName(info);
                break;
            case 3: setCnpdAllStatsInPkts(Long.parseLong(info));
                break;
            case 4: setCnpdAllStatsOutPkts(Long.parseLong(info));
                break;
            case 5: setCnpdAllStatsInBytes(Long.parseLong(info));
                break;
            case 6: setCnpdAllStatsOutBytes(Long.parseLong(info));
                break;
            case 7: setCnpdAllStatsHCInPkts(Long.parseLong(info));
                break;
            case 8: setCnpdAllStatsHCOutPkts(Long.parseLong(info));
                break;
            case 9: setCnpdAllStatsHCInBytes(Long.parseLong(info));
                break;
            case 10: setCnpdAllStatsHCOutBytes(Long.parseLong(info));
                break;
            case 11: setCnpdAllStatsInBitRate(Long.parseLong(info));
                break;
            case 12: setCnpdAllStatsOutBitRate(Long.parseLong(info));
                break;
        }
    }
}
