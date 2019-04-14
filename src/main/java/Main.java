/**
 * Main class to execute program.
 */
public class Main {
    /**
     * Main method.
     * @param args args from command line.
     * @throws Exception when program failed.
     */
    public static void main(String[] args) throws Exception{
        System.out.println("Hello World!");
        Config cfg = new Config("./config.yaml");
    }
}