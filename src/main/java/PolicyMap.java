import java.util.ArrayList;

/**
 * Class for describing Policy Maps.
 */
public class PolicyMap {
    /**
     * Name of PMAP.
     */
    String name;
    /**
     * CMAP which included by PMAP.
     */
    ClassMap classMap;
    /**
     * Bbandwidth shaper for protocol in CMAP.
     */
    long shaper;
    /**
     * Time to live for PMAP.
     */
    int TTL;

    /**
     * Method to get list of reverse commands for PMAP.
     * @return list of reverse commands.
     */
    public ArrayList<String> getReverceCommands() {
        return reverceCommands;
    }

    /**
     * Method to set list of reverse commands for PMAP.
     * @param reverceCommands list of reverse commands.
     */
    public void setReverceCommands(ArrayList<String> reverceCommands) {
        this.reverceCommands = reverceCommands;
    }

    /**
     * List of reverse commands for PMAP.
     */
    ArrayList<String> reverceCommands;

    /**
     * Method to get time to live for PMAP
     * @return time to live
     */
    public int getTTL() {
        return TTL;
    }

    /**
     * Method to set time to live for PMAP
     */
    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    /**
     * Method to get CMAP for PMAP
     * @return CMAP
     */
    public ClassMap getClassMap() {
        return classMap;
    }

    /**
     * Method to get name of PMAP
     * @return string with name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get shaper for PMAP
     * @return bandwidth for protocol in CMAP
     */
    public long getShaper() {
        return shaper;
    }

    /**
     * Constructor for Policy Map
     * @param name name of PMAP
     * @param classMap CMAP for PMAP
     * @param shaper bandwidth for protocol in CMAP
     */
    public PolicyMap(String name,ClassMap classMap, long shaper) {
        this.name = name;
        this.classMap = classMap;
        this.shaper = shaper;
    }
}
