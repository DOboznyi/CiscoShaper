import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Config {
    String name;
    String user;
    String password;
    String community;
    ArrayList<String> Protocols;
    ArrayList<Host> hosts;

    public Config(String Path){
        hosts = new ArrayList<Host>();
        ArrayList<String> list = new ArrayList<String>();
        try
        {
            Scanner s = new Scanner(new File(Path));
            while (s.hasNext()){
                list.add(s.next());
            }
            s.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File does Not Exist Please Try Again: ");
        }
        for (int i = 0; i<list.size();i++) {
            if (list.get(i).contains("host:")){
                i++;
                if (list.get(i).contains("name:")){
                    i++;
                    String name = list.get(i);
                    i++;
                    if (list.get(i).contains("user:")) {
                        i++;
                        String user = list.get(i);
                        i++;
                        if (list.get(i).contains("password:")) {
                            i++;
                            String password = list.get(i);
                            i++;
                            if (list.get(i).contains("community:")) {
                                i++;
                                String community = list.get(i);
                                i++;
                                if (list.get(i).contains("protocols:")) {
                                    ArrayList<String> protocols = new ArrayList<String>();
                                    while(i+1<list.size()&&list.get(i+1).contains("-")&&!list.get(i+1).contains("host:")) {
                                        i++;
                                        if (list.get(i).contains("-")) {
                                            i++;
                                            String protocol = list.get(i);
                                            protocols.add(protocol);
                                        }
                                    }
                                    Host host = new Host(name,user,password,community,protocols);
                                    hosts.add(host);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<PolicyMap> getPolicyMaps(){
        SshClient ssh = new SshClient(user,name,password);
        ArrayList<String> results = ssh.executeCommand("show policy-map");
        return null;
    }
}
