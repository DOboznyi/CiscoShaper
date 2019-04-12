import java.util.ArrayList;

/**
 * Class for describing port.
 */
public class Port {
    /**
     * Index of the port.
     */
    int ifIndex;
    /**
     * List of snapshots for port.
     */
    ArrayList<Snapshot> snapshotList;
    /**
     * List of timestamps for port.
     */
    ArrayList<Timestamp> TimestampList;

    /**
     * Method to get index of port.
     * @return index of port.
     */
    public int getIfIndex() {
        return ifIndex;
    }

    /**
     * Method to get list of timestamps for port.
     * @return list of timestamps.
     */
    public ArrayList<Timestamp> getTimestampList() {
        return TimestampList;
    }

    /**
     * Method to set list of timestamps for port.
     */
    public void setTimestampList(ArrayList<Timestamp> trafficList) {
        TimestampList = trafficList;
    }

    /**
     * Constructor for port.
     * @param ifIndex index of port.
     */
    public Port (int ifIndex){
        this.ifIndex=ifIndex;
        TimestampList = new ArrayList<Timestamp>();
        snapshotList = new ArrayList<Snapshot>();
    }

    /**
     * Method to set index of port.
     */
    public void setIfIndex(int ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * Method to add info to the snapshots.
     */
    public void addInfo(String info, int Column, long Time){
        if (snapshotList.size()==0||snapshotList.get(snapshotList.size()-1).Time!= Time)snapshotList.add(new Snapshot());
        snapshotList.get(snapshotList.size()-1).addInfo(info,Column,Time);
    }
}
