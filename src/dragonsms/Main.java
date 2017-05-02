package dragonsms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {

    // Driver program
    public static void main(String[] args) throws FileNotFoundException {

        // load Spring and Hibernate
        SpringDriver.run();
        DragonServer server = new DragonServer();

        // comment out to read from file
        server.io.setIn(new FileInputStream("src/dragonsms/dragon-in.txt"));

        // comment out to write to file
        // server.io.setOut(new FileOutputStream("src/dragonsms/dragon-out.txt"));

        // start the server thread
        server.io.println("Welcome to DragonSMS! Send HINT to get started!");
        server.start();

    }
}
