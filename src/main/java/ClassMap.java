/**
 * Class for describing Class Maps.
 */
public class ClassMap {
    /**
     * Match criteria. Match-all or match-any.
     */
    String match;
    /**
     * Name of CMAP.
     */
    String name;
    /**
     * Name of protocol which blocking.
     */
    String protocol;

    /**
     * Constructor for CMAP.
     * @param match match criteria. Match-all or match-any.
     * @param name name of CMAP.
     * @param protocol name of protocol which blocking.
     */
    public ClassMap(String match, String name, String protocol) {
        if (match!= null)this.match = match;
        else this.match = "all";
        this.name = name;
        this.protocol = protocol;
    }

    /**
     * Method for getting match criteria.
     * @return match criteria. Match-all or match-any.
     */
    public String getMatch() {

        return match;
    }

    /**
     * Method for getting name of CMAP.
     * @return name of CMAP.
     */
    public String getName() {
        return name;
    }

    /**
     * Method for getting name of protocol which blocking.
     * @return protocol which blocking.
     */
    public String getProtocol() {
        return protocol;
    }
}
