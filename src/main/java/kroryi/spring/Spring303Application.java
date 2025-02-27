package kroryi.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "kroryi.spring.repository")
@SpringBootApplication
public class Spring303Application {

    public static void main(String[] args) {
        SpringApplication.run(Spring303Application.class, args);
    }

}
