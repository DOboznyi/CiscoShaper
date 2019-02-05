import java.util.HashMap;

public class Timestamp {
    HashMap<Integer, Traffic> dump;
    long Time;

    public HashMap<Integer, Traffic> getDump() {
        return dump;
    }

    public void setDump(HashMap<Integer, Traffic> dump) {
        this.dump = dump;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public Timestamp(long time) {
        Time = time;
        dump = new HashMap<Integer, Traffic>();
    }

    public void addInfo(String info, int column, int index){
        if (dump.get(index)==null) dump.put(index,new Traffic(index));
        dump.get(index).addInfo(info,column);
    }
}
