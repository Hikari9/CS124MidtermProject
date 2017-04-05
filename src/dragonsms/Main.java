package dragonsms;

public class Main {

    // Driver program
    public static void main(String[] args) {

        // load Spring
        SpringDriver.run();

        // create dragon server from System I/O
        DragonServer server = new DragonServer(System.in, System.out);
        System.out.println("Welcome to DragonSMS! Send HINT to get started!");
        server.start();

    }

}
