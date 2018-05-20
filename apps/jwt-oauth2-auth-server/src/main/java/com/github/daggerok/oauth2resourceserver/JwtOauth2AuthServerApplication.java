package com.github.daggerok.oauth2resourceserver;

import com.github.daggerok.thymeleaf.PropsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ PropsAutoConfiguration.class })
public class JwtOauth2AuthServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtOauth2AuthServerApplication.class, args);
  }
}
