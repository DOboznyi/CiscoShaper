import java.util.ArrayList;

public class Host {
    String name;
    String user;
    String password;
    String community;
    ArrayList<String> protocols;

    public Host(String name, String user, String password, String community, ArrayList<String> protocols) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.community = community;
        this.protocols = protocols;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getCommunity() {
        return community;
    }

    public ArrayList<String> getProtocols() {
        return protocols;
    }
}
