import java.util.ArrayList;

public class PolicyMap {
    String name;
    ClassMap classMap;
    long shaper;

    public ArrayList<String> getReverceCommands() {
        return reverceCommands;
    }

    public void setReverceCommands(ArrayList<String> reverceCommands) {
        this.reverceCommands = reverceCommands;
    }

    ArrayList<String> reverceCommands;

    public int getTTL() {
        return TTL;
    }

    public void setTTL(int TTL) {
        this.TTL = TTL;
    }

    int TTL;

    public ClassMap getClassMap() {
        return classMap;
    }

    public String getName() {
        return name;
    }

    public long getShaper() {
        return shaper;
    }

    public PolicyMap(String name,ClassMap classMap, long shaper) {
        this.name = name;
        this.classMap = classMap;
        this.shaper = shaper;
    }
}
