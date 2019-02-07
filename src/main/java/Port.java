import java.util.ArrayList;
import java.util.HashMap;

public class Port {
    int ifIndex;
    ArrayList<Snapshot> snapshotList;
    ArrayList<Timestamp> TimestampList;

    public int getIfIndex() {
        return ifIndex;
    }

    public ArrayList<Timestamp> getTimestampList() {
        return TimestampList;
    }

    public void setTimestampList(ArrayList<Timestamp> trafficList) {
        TimestampList = trafficList;
    }

    public Port (int ifIndex){
        this.ifIndex=ifIndex;
        TimestampList = new ArrayList<Timestamp>();
        snapshotList = new ArrayList<Snapshot>();
    }

    public void setIfIndex(int ifIndex) {
        this.ifIndex = ifIndex;
    }

    public void addInfo(String info, int Column, long Time){
        if (snapshotList.size()==0||snapshotList.get(snapshotList.size()-1).Time!= Time)snapshotList.add(new Snapshot());
        snapshotList.get(snapshotList.size()-1).addInfo(info,Column,Time);
    }
}
