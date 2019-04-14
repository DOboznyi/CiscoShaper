import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;
import java.util.*;

/**
 * Class for describing SSH client.
 */
class SSHClient
{
    /**
     * User name for connecting via SSH.
     */
    String user;
    /**
     * Name of the host. IP address or domain name.
     */
    String host;
    /**
     * Password for connecting via SSH.
     */
    String passwd;

    /**
     * Constructor for SSH client.
     * @param user user name for connecting via SSH.
     * @param host name of the host. IP address or domain name.
     * @param passwd password for connecting via SSH.
     */
    SSHClient(String user,String host,String passwd){
        this.user = user;
        this.host = host;
        this. passwd = passwd;
    }

    /**
     * Method to run list of commands via SSH.
     * @param commands list of commands.
     * @throws Exception when connection aborted.
     */
    public void RunCommands(ArrayList<String> commands) throws Exception
    {
        JSch jsch = new JSch();
        int port = 22;
        Session session = jsch.getSession(user, host, port);
        session.setPassword(passwd);

        session.setConfig("StrictHostKeyChecking", "no");

        session.connect();

        Channel channel = session.openChannel("shell");
        OutputStream ops = channel.getOutputStream();
        PrintStream ps = new PrintStream(ops, true);

        channel.connect();
        InputStream input = channel.getInputStream();

        //commands
        for (String command:commands
             ) {
            ps.println(command);
        }
        ps.close();

        printResult(input, channel);

        channel.disconnect();
        session.disconnect();
    }

    /**
     * Method to print results after executing list of commands.
     * @param input input stream from device.
     * @param channel channel from device.
     * @return list of strings with results of executed commands.
     * @throws Exception when connection aborted.
     */
    private ArrayList<String> printResult(InputStream input, Channel channel) throws Exception
    {
        ArrayList<String> result = new ArrayList<String>();
        int SIZE = 1024;
        byte[] tmp = new byte[SIZE];
        while (true)
        {
            while (input.available() > 0)
            {
                int i = input.read(tmp, 0, SIZE);
                if(i < 0)
                    break;
                result.add(tmp.toString());
                System.out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed())
            {
                result.add(tmp.toString());
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try
            {
                Thread.sleep(300);
            }
            catch (Exception ee)
            {
            }
        }
        return result;
    }

    /**
     * Method to run single command via SSH.
     * @param command command which may be executed.
     * @return result of execution.
     */
    public ArrayList<String> executeCommand(String command)
    {
        int port = 22;
        ArrayList<String> result = new ArrayList<String>();
        try
        {
            JSch jsch = new JSch();

         /*
         * Open a new session, with your username, host and port
         * Set the password and call connect.
         * session.connect() opens a new connection to remote SSH server.
         * Once the connection is established, you can initiate a new channel.
         * this channel is needed to connect to remotely execution program
         */
            Session session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(passwd);
            session.connect();

            //create the excution channel over the session
            ChannelExec channelExec = (ChannelExec)session.openChannel("exec");

            // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
            InputStream in = channelExec.getInputStream();

            // Set the command that you want to execute
            // In our case its the remote shell script
            channelExec.setCommand(command);

            // Execute the command
            channelExec.connect();

            // Read the output from the input stream we set above
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            //Read each line from the buffered reader and add it to result list
            // You can also simple print the result here
            while ((line = reader.readLine()) != null)
            {
                result.add(line);
            }

            //retrieve the exit status of the remote command corresponding to this channel
            int exitStatus = channelExec.getExitStatus();

            //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
            channelExec.disconnect();
            session.disconnect();

            if(exitStatus < 0){
                // System.out.println("Done, but exit status not set!");
            }
            else if(exitStatus > 0){
                // System.out.println("Done, but with error!");
            }
            else{
                // System.out.println("Done!");
            }

        }
        catch(Exception e)
        {
            System.err.println("Error: " + e);
        }
        return result;
    }
}