package com.github.daggerok.oauth2resourceserver;

import com.github.daggerok.thymeleaf.PropsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
class RemoteTokenServicesConfig {
  //tag::remote-token-services-config[]
  @Bean
  @Primary
  public RemoteTokenServices tokenService() {
      final RemoteTokenServices tokenService = new RemoteTokenServices();
      tokenService.setCheckTokenEndpointUrl(
      "http://localhost:8080/spring-security-oauth-server/oauth/check_token");
      tokenService.setClientId("fooClientIdPassword");
      tokenService.setClientSecret("secret");
      return tokenService;
  }
  //end::remote-token-services-config[]
}

@SpringBootApplication
@Import({ PropsAutoConfiguration.class })
public class Oauth2ResourceServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2ResourceServerApplication.class, args);
  }
}
