package com.github.daggerok.oauth2authserver;

import com.github.daggerok.props.ClientsPropsAutoConfiguration;
import com.github.daggerok.props.config.ClientsProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;

import static com.github.daggerok.props.config.ClientsProps.Client;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
class DataSourceConfig {

  //tag::datasource-initializer[]
  @Bean
  public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
    final DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator());
    return initializer;
  }

  private DatabasePopulator databasePopulator() {
    final ClassPathResource schema = new ClassPathResource("/schema-h2.sql", DataSourceConfig.class.getClassLoader());
    return new ResourceDatabasePopulator(false, true, UTF_8.displayName(), schema);
  }
  //end::datasource-initializer[]
}

@Configuration
class PasswordEncoderConfig {
  //tag::password-encoder[]
  @Bean
  public PasswordEncoder encoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
  //end::password-encoder[]
}

//tag::jdbc-user-details-service[]
@Service
@RequiredArgsConstructor
class JdbcUserDetailsService implements UserDetailsService {

  final PasswordEncoder encoder;
  final JdbcTemplate jdbcTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return User
        .withUsername("usr")
        //.password("pwd")
        .password(null == encoder ? "{noop}pwd" : encoder.encode("pwd"))
        .disabled(false)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .authorities("USER", "ADMIN", "ROLE_USER", "ROLE_ADMIN")
        .build();
  }
}
//end::jdbc-user-details-service[]

//tag::authentication-manager-config[]
@Configuration
@RequiredArgsConstructor
class AuthenticationManagerConfig extends WebSecurityConfigurerAdapter {

  final JdbcUserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
//end::authentication-manager-config[]

//tag::jdbc-oauth2-auth-server-config[]
/**
 * CORS Filter.
 *
 * https://github.com/spring-projects/spring-security-oauth/issues/938#issuecomment-286243307
 * @Orger(2)
 * +
 * @Bean FilterRegistrationBean corsFilter() { ... }
 */
@Order(2)
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
class JdbcOauth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

  final DataSource dataSource;
  final PasswordEncoder encoder;
  final ClientsProps clientsProps;
  final AuthenticationManager authenticationManager;

  @Bean
  public TokenStore tokenStore() {
    return new JdbcTokenStore(dataSource);
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {

    final Client resourceAppClient = clientsProps.getResourceAppClient();
    final Client passwordClient = clientsProps.getPasswordClient();

    clients
        .jdbc(dataSource)
          .withClient(resourceAppClient.getClientId())
            .authorizedGrantTypes(resourceAppClient.getAuthorizedGrantTypes())
            .secret(resourceAppClient.generateSecret(encoder))
            .scopes(resourceAppClient.getScopes())
            .autoApprove(resourceAppClient.isAutoApprove())
            .and()
          .withClient(passwordClient.getClientId())
            .authorizedGrantTypes(passwordClient.getAuthorizedGrantTypes())
            .secret(passwordClient.generateSecret(encoder))
            .scopes(passwordClient.getScopes())
            .autoApprove(passwordClient.isAutoApprove())
    ;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore())
        .authenticationManager(authenticationManager)
    ;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

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
//end::jdbc-oauth2-auth-server-config[]

@SpringBootApplication
@Import({
    ClientsPropsAutoConfiguration.class,
})
public class JdbcOauth2AuthServer {

  public static void main(String[] args) {
    SpringApplication.run(JdbcOauth2AuthServer.class, args);
  }
}
