package br.com.projetofinal_grupo1.projetofinal_grupo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EntityScan(basePackages = "br.com.projetofinal_grupo1.projetofinal_grupo1.model")
@EnableFeignClients(basePackages = "br.com.projetofinal_grupo1.projetofinal_grupo1.feign")
public class ProjetofinalGrupo1Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjetofinalGrupo1Application.class, args);
	}

}
