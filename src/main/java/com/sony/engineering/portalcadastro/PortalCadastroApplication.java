package com.sony.engineering.portalcadastro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.sony.engineering.portalcadastro.model")
@EnableJpaRepositories("com.sony.engineering.portalcadastro.repository")
public class PortalCadastroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalCadastroApplication.class, args);
	}

}
