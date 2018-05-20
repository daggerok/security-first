package com.github.daggerok.oauth2authserver;

import com.github.daggerok.thymeleaf.PropsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ PropsAutoConfiguration.class })
public class JdbcOauth2AuthServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JdbcOauth2AuthServerApplication.class, args);
  }
}
