/**
 * Class to store data of traffic at current time.
 */
public class Traffic {
    /**
     * An object which represents a unique
     * identifier for a protocol or application
     * which NBAR currently recognizes.
     */
    int cnpdAllStatsProtocolsIndex;
    /**
     * This object reflects the valid string of a protocol
     * or application which NBAR currently recognizes.
     */
    String cnpdAllStatsProtocolsName;
    /**
     * The packet count of inbound packets as determined by Protocol Discovery.
     */
    long cnpdAllStatsInPkts;
    /**
     * The packet count of outbound packets as determined by Protocol Discovery.
     */
    long cnpdAllStatsOutPkts;
    /**
     * The byte count of inbound octets as determined by Protocol Discovery.
     */
    long cnpdAllStatsInBytes;
    /**
     * The byte count of outbound octets as determined by Protocol Discovery.
     */
    long cnpdAllStatsOutBytes;
    /**
     * The packet count of inbound packets as determined by Protocol Discovery.
     * This is the 64-bit (High Capacity) version of cnpdAllStatsInPkts.
     */
    long cnpdAllStatsHCInPkts;
    /**
     * The packet count of outbound packets as determined by Protocol Discovery.
     * This is the 64-bit (High Capacity) version of cnpdAllStatsOutPkts.
     */
    long cnpdAllStatsHCOutPkts;
    /**
     * The byte count of inbound octets as determined by Protocol Discovery.
     * This is the 64-bit (High Capacity) version of cnpdAllStatsInBytes.
     */
    long cnpdAllStatsHCInBytes;
    /**
     * The byte count of outbound octets as determined by Protocol Discovery.
     * This is the 64-bit (High Capacity) version of cnpdAllStatsOutBytes.
     */
    long cnpdAllStatsHCOutBytes;
    /**
     * The inbound bit rate as determined by Protocol Discovery.
     */
    long cnpdAllStatsInBitRate;
    /**
     * The outbound bit rate as determined by Protocol Discovery.
     */
    long cnpdAllStatsOutBitRate;
    /**
     * Current time.
     */
    long Time;

    /**
     * Constructor for traffic object.
     * @param cnpdAllStatsProtocolsIndex An object which represents a unique
     *                                   identifier for a protocol or application
     *                                   which NBAR currently recognizes.
     */
    public Traffic(int cnpdAllStatsProtocolsIndex) {
        this.cnpdAllStatsProtocolsIndex = cnpdAllStatsProtocolsIndex;
    }

    /**
     * Method for getting the cnpdAllStatsProtocolsName.
     * @return valid string of a protocol or application which NBAR currently recognizes.
     */
    public String getCnpdAllStatsProtocolsName() {
        return cnpdAllStatsProtocolsName;
    }

    /**
     * Method for getting the cnpdAllStatsInBytes.
     * @return The packet count of inbound packets as determined by Protocol Discovery.
     */
    public long getCnpdAllStatsInBytes() {
        return cnpdAllStatsInBytes;
    }

    /**
     * Method for getting current time.
     * @return current time.
     */
    public long getTime() {
        return Time;
    }

    /**
     * Method too set current time.
     * @param time current time
     */
    public void setTime(long time) {
        Time = time;
    }

    /**
     * Method to update info in traffic table.
     * @param info string with information.
     * @param Column column in cnpdAllStatsTable.
     */
    public void addInfo(String info, int Column){
        switch (Column){
            case 1: cnpdAllStatsProtocolsIndex=Integer.parseInt(info);
                break;
            case 2: cnpdAllStatsProtocolsName=info;
                break;
            case 3: cnpdAllStatsInPkts=Long.parseLong(info);
                break;
            case 4: cnpdAllStatsOutPkts=Long.parseLong(info);
                break;
            case 5: cnpdAllStatsInBytes=Long.parseLong(info);
                break;
            case 6: cnpdAllStatsOutBytes=Long.parseLong(info);
                break;
            case 7: cnpdAllStatsHCInPkts=Long.parseLong(info);
                break;
            case 8: cnpdAllStatsHCOutPkts=Long.parseLong(info);
                break;
            case 9: cnpdAllStatsHCInBytes=Long.parseLong(info);
                break;
            case 10: cnpdAllStatsHCOutBytes=Long.parseLong(info);
                break;
            case 11: cnpdAllStatsInBitRate=Long.parseLong(info);
                break;
            case 12: cnpdAllStatsOutBitRate=Long.parseLong(info);
                break;
        }
    }
}
