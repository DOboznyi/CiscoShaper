import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Config {
    String name;
    String user;
    String password;
    String community;
    ArrayList<String> Protocols;

    public Config(String Path){
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
            String str = ;
            if (str.contains())
        }
    }

    private ArrayList<PolicyMap> getPolicyMaps(){
        SshClient ssh = new SshClient(user,name,password);
        ArrayList<String> results = ssh.executeCommand("show policy-map");

    }
}
