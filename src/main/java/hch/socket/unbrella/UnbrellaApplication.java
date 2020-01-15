package hch.socket.unbrella;

import hch.socket.unbrella.netty.demo5.HeartBeatServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnbrellaApplication {

    public static void main(String[] args) {
        new HeartBeatServer(8088).start();
        SpringApplication.run(UnbrellaApplication.class, args);
    }

}
