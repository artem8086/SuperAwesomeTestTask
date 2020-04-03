package art.soft.test;

import com.mongodb.MongoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@SpringBootApplication
public class SuperAwesomeTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuperAwesomeTestTaskApplication.class, args);
	}
}
