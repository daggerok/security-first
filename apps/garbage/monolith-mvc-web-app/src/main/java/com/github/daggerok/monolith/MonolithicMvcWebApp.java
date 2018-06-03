package com.github.daggerok.monolith;

import com.github.daggerok.props.PropsAutoConfiguration;
import com.github.daggerok.props.config.AppProps;
import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

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

@Controller
@RequiredArgsConstructor
class IndexPage {

  final AppProps props;

  @ModelAttribute("props")
  public AppProps props() {
    return props;
  }

  @GetMapping({"", "/", "/404"})
  public String index() {
    return "index";
  }
}

@SpringBootApplication
@Import({ PropsAutoConfiguration.class})
public class MonolithicMvcWebApp {

  public static void main(String[] args) {
    SpringApplication.run(MonolithicMvcWebApp.class, args);
  }
}
