package dragonsms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {

    // Driver program
    public static void main(String[] args) throws FileNotFoundException {
        SpringDriver.run(); // load Spring

        // it is possible to create two servers with their own modules
        // first server in error stream, second server in output stream
        DragonServer dragonServerIO = new DragonServer(System.in, System.err);
        DragonServer dragonServerFile = new DragonServer(new FileInputStream("src/dragonsms/dragon.txt"),
                                                         System.out);

        // start the two servers
        dragonServerIO.start();
        dragonServerFile.start();

        System.out.println("Welcome to DragonSMS! Send HINT to get started!");

    }
}
