import java.util.HashMap;

/**
 * Class for describing Timestamp.
 */
public class Timestamp {
    /**
     * HashMap with dumps of traffic at current time/
     */
    HashMap<Integer, Traffic> dump;
    /**
     * Current time.
     */
    long Time;

    /**
     * Method to get HashMap with traffic dump at current time.
     * @return HashMap with traffic dump.
     */
    public HashMap<Integer, Traffic> getDump() {
        return dump;
    }

    /**
     * Method to set HashMap with traffic dump at current time.
     * @param dump dump of traffic at current time.
     */
    public void setDump(HashMap<Integer, Traffic> dump) {
        this.dump = dump;
    }

    /**
     * Method to get current time.
     * @return current time.
     */
    public long getTime() {
        return Time;
    }

    /**
     * Method to set current time.
     * @param time current time
     */
    public void setTime(long time) {
        Time = time;
    }

    /**
     * Constructor for Timestamp.
     * @param time current time.
     */
    public Timestamp(long time) {
        Time = time;
        dump = new HashMap<Integer, Traffic>();
    }

    /**
     * Method to add info to the table with dump of network usage at current time.
     * @param info information.
     * @param column column in table.
     * @param index index in table.
     */
    public void addInfo(String info, int column, int index){
        if (dump.get(index)==null) dump.put(index,new Traffic(index));
        dump.get(index).addInfo(info,column);
    }
}
