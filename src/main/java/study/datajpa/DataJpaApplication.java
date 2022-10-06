package study.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import study.datajpa.repository.TestRepository;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing // auditing 기능 넣어야한다.
@SpringBootApplication
// @EnableJpaRepositories(basePackages = {...}) spring boot 로 넘어가지게 되면서 필요없게 바뀌었다. 하위 패키지들 모두 스캔
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);

	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
