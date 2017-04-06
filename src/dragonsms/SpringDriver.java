package dragonsms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDriver {
    public static void run() {
        new ClassPathXmlApplicationContext("classpath*:**/applicationContext*.xml");
        System.err.println("[Successfully loaded Spring application context]");
    }
}