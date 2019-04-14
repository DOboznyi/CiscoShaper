/**
 * Class for describing stats of port.
 */
public class Snapshot {
    /**
     * A textual string containing information about the interface.
     */
    String ifDescr;
    /**
     * The type of interface.
     */
    long ifType;
    /**
     * The size of the largest packet which can be sent/received on the interface, specified in octets.
     */
    long ifMtu;
    /**
     * An estimate of the interface's current bandwidth in bits per second.
     */
    long ifSpeed;
    /**
     * The interface's address at its protocol sub-layer.
     */
    String ifPhysAddress;
    /**
     * The desired state of the interface.
     */
    long ifAdminStatus;
    /**
     * The current operational state of the interface.
     */
    long ifOperStatus;
    /**
     * The value of sysUpTime at the time the interface entered its current operational state.
     */
    String ifLastChange;
    /**
     * The total number of octets received on the interface, including framing characters.
     */
    long ifInOctets;
    /**
     * The number of packets, delivered by this sub-layer to a higher (sub-)layer,
     * which were not addressed to a multicast or broadcast address at this sub-layer.
     */
    long ifInUcastPkts;
    /**
     * he number of packets, delivered by this sub-layer to a higher (sub-)layer,
     * which were addressed to a multicast or broadcast address at this sub-layer.
     */
    long ifInNUcastPkts;
    /**
     * The number of inbound packets which were chosen to be discarded even
     * though no errors had been detected to prevent their being deliverable
     * to a higher-layer protocol.
     */
    long ifInDiscards;
    /**
     * For packet-oriented interfaces, the number of inbound packets
     * that contained errors preventing them from being deliverable to a higher-layer protocol.
     */
    long ifInErrors;
    /**
     * For packet-oriented interfaces, the number of packets received
     * via the interface which were discarded because of an unknown or unsupported protocol.
     */
    long ifInUnknownProtos;
    /**
     * The total number of octets transmitted out of the interface, including framing characters.
     */
    long ifOutOctets;
    /**
     * The total number of packets that higher-level protocols requested be transmitted,
     * and which were not addressed to a multicast or broadcast address at this sub-layer,
     * including those that were discarded or not sent.
     */
    long ifOutUcastPkts;
    /**
     * The total number of packets that higher-level protocols requested be transmitted,
     * and which were addressed to a multicast or broadcast address at this sub-layer,
     * including those that were discarded or not sent.
     */
    long ifOutNUcastPkts;
    /**
     * The number of outbound packets which were chosen to be discarded even
     * though no errors had been detected to prevent their being transmitted.
     */
    long ifOutDiscards;
    /**
     * For packet-oriented interfaces, the number of outbound packets that
     * could not be transmitted because of errors.
     */
    long ifOutErrors;
    /**
     * The length of the output packet queue (in packets).
     */
    long ifOutQLen;
    /**
     * A reference to MIB definitions specific to the particular media being used to realize the interface.
     */
    long ifSpecific;
    /**
     * This object is an 'alias' name for the interface as specified by a network manager,
     * and provides a non-volatile 'handle' for the interface.
     */
    String ifAlias;
    /**
     * Current time.
     */
    long Time;
    /**
     * Real speed of link channel.
     */
    long realSpeed;

    /**
     * Method to set real speed link channel.
     * @param realSpeed value of real speed link channel
     */
    public void setRealSpeed(long realSpeed) {
        this.realSpeed = realSpeed;
    }

    /**
     * Method for getting alias of interface.
     * @return string with alias of interface.
     */
    public String getIfAlias() {
        return ifAlias;
    }

    /**
     * Method to set alias of interface.
     * @param ifAlias string with alias of interface.
     */
    public void setIfAlias(String ifAlias) {
        this.ifAlias = ifAlias;
    }

    /**
     * Method to get description of protocol.
     * @return string with interface description.
     */
    public String getIfDescr() {
        return ifDescr;
    }

    /**
     * Method to get the total number of octets received on the interface, including framing characters.
     * @return total number of octets received on the interface
     */
    public long getIfInOctets() {
        return ifInOctets;
    }

    /**
     * Method to add info to the table with dump of network usage at current time.
     * @param info information.
     * @param column column in table.
     * @param time index in table.
     */
    public void addInfo(String info, int column, long time){
        this.Time = time;
        switch (column){
            case 2: ifDescr=info;
                break;
            case 3: ifType=Long.parseLong(info);
                break;
            case 4: ifMtu=Long.parseLong(info);
                break;
            case 5: ifSpeed=Long.parseLong(info);
                break;
            case 6: ifPhysAddress=info;
                break;
            case 7: ifAdminStatus=Long.parseLong(info);
                break;
            case 8: ifOperStatus=Long.parseLong(info);
                break;
            case 9: ifLastChange=info;
                break;
            case 10: ifInOctets=Long.parseLong(info);
                break;
            case 11: ifInUcastPkts=Long.parseLong(info);
                break;
            case 12: ifInNUcastPkts=Long.parseLong(info);
                break;
            case 13: ifInDiscards=Long.parseLong(info);
                break;
            case 14: ifInErrors=Long.parseLong(info);
                break;
            case 15: ifInUnknownProtos=Long.parseLong(info);
                break;
            case 16: ifOutOctets=Long.parseLong(info);
                break;
            case 17: ifOutUcastPkts=Long.parseLong(info);
                break;
            case 18: ifOutNUcastPkts=Long.parseLong(info);
                break;
            case 19: ifOutDiscards=Long.parseLong(info);
                break;
            case 20: ifOutErrors=Long.parseLong(info);
                break;
            case 21: ifOutQLen=Long.parseLong(info);
                break;
            case 22: ifSpecific=Long.parseLong(info);
                break;
        }
    }
}
