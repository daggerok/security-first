package com.github.daggerok.props.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "clients")
public class ClientsProps {

  Client resourceAppClient, passwordClient;

  @Data
  public static class Client {

    String[] authorizedGrantTypes, scopes;
    String clientId, secret;
    boolean autoApprove;

    public String generateSecret(final PasswordEncoder encoder) {
      return null == encoder ? "{noop}" + secret : encoder.encode(secret);
    }
  }
}
