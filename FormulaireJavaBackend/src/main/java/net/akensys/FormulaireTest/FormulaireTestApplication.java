package net.akensys.FormulaireTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "net.akensys.FormulaireTest.repository")
@ComponentScan(basePackages = "net.akensys.FormulaireTest")
public class FormulaireTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormulaireTestApplication.class, args);
	}

}
