package com.sony.engineering.portalcadastro;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.sony.engineering.portalcadastro"})
@EntityScan("com.sony.engineering.portalcadastro.model")
@EnableJpaRepositories("com.sony.engineering.portalcadastro.repository")
public class PortalCadastroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalCadastroApplication.class, args);
	}

}
