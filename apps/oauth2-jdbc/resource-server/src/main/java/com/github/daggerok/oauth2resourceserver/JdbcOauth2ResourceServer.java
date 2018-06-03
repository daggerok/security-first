package com.github.daggerok.oauth2resourceserver;

import com.github.daggerok.props.AppPropsAutoConfiguration;
import com.github.daggerok.props.ClientsPropsAutoConfiguration;
import com.github.daggerok.props.config.AppProps;
import com.github.daggerok.props.config.ClientsProps;
import io.vavr.collection.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Instant;

import static java.lang.String.format;

@RestController
class AppResources {

  @GetMapping
  @PreAuthorize("#oauth2.hasScope('read')")
  public ResponseEntity api() {
    return ResponseEntity.ok(HashMap.of(
        "ololo", "trololo",
        "at", Instant.now()
    ).toJavaMap());
  }
}

@Configuration
@RequiredArgsConstructor
class RemoteTokenServicesConfig {
  //tag::remote-token-services-config[]
  final AppProps appProps;
  final ClientsProps clientsProps;

  @Bean
  @Primary
  public RemoteTokenServices tokenService() {

    final String checkTokenEndpointUrl = format("%s/oauth/check_token", appProps.getAuthServerUrl());
    final RemoteTokenServices tokenService = new RemoteTokenServices();
    tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
    final ClientsProps.Client client = clientsProps.getResourceAppClient();
    tokenService.setClientId(client.getClientId());
    tokenService.setClientSecret(client.getSecret());
    return tokenService;
  }
  //end::remote-token-services-config[]
}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OAuth2ResourceServerConfig extends GlobalMethodSecurityConfiguration {

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    return new OAuth2MethodSecurityExpressionHandler();
  }
}

@Order(2)
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebMvcResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Bean
  public FilterRegistrationBean corsFilter() {

    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    final CorsFilter corsFilter = new CorsFilter(source);
    final FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return bean;
  }
}
/*
{
  public MethodSecurityExpressionHandler createExpressionHandler() {
    return new OAuth2MethodSecurityExpressionHandler();
  }
}
*/

@SpringBootApplication
@Import({
    AppPropsAutoConfiguration.class,
    ClientsPropsAutoConfiguration.class,
})
public class JdbcOauth2ResourceServer {

  public static void main(String[] args) {
    SpringApplication.run(JdbcOauth2ResourceServer.class, args);
  }
}
