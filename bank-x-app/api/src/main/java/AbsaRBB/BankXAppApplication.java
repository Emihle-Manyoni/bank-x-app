package AbsaRBB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankXAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankXAppApplication.class, args);
	}

}
