package com.github.daggerok.jwtoauth2authserver;

import com.github.daggerok.thymeleaf.PropsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ PropsAutoConfiguration.class })
public class Oauth2ResourceServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2ResourceServerApplication.class, args);
  }
}
