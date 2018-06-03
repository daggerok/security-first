package com.github.daggerok.oauth2resourceserver;

import com.github.daggerok.props.AppPropsAutoConfiguration;
import com.github.daggerok.props.config.AppProps;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
class ThymeleafConfig implements WebMvcConfigurer {

  @Bean
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

  @Bean
  TemplateEngine templateEngine() {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addDialect(layoutDialect());
    return templateEngine;
  }
}

@Slf4j
@ControllerAdvice
class GlobalApplicationErrorHandler {

  @SneakyThrows
  @ExceptionHandler(Throwable.class)
  public void handleErrors(final HttpServletResponse response, final Throwable error) {
    final String message = Try.of(error::getLocalizedMessage).getOrElseGet(throwable -> "Unknown error");
    log.error("handling: {}", message);
    response.sendRedirect("/");
  }
}

@Controller
@RequiredArgsConstructor
class ApplicationPage {

  final AppProps appProps;

  @GetMapping({ "", "/", "/404", "/error" })
  public String index() {
    return "index";
  }

  @ResponseBody
  @GetMapping("/api/config")
  public ResponseEntity config() {
    return ResponseEntity.ok(HashMap.of(
        "appProps", appProps
    ).toJavaMap());
  }
}

@SpringBootApplication
@Import({
    AppPropsAutoConfiguration.class,
})
public class JdbcOauth2WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(JdbcOauth2WebApplication.class, args);
  }
}
