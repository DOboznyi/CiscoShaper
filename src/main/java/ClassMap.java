public class ClassMap {
    String match;
    String name;
    String protocol;

    public ClassMap(String match, String name, String protocol) {
        if (match!= null)this.match = match;
        else this.match = "all";
        this.name = name;
        this.protocol = protocol;
    }

    public String getMatch() {

        return match;
    }

    public String getName() {
        return name;
    }

    public String getProtocol() {
        return protocol;
    }
}
