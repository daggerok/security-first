package com.github.daggerok.monolith;

import com.github.daggerok.props.PropsAutoConfiguration;
import com.github.daggerok.props.config.AppProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ThymeleafProperties.class)
class WebfluxThymeleafConfig implements WebFluxConfigurer {

  final ISpringWebFluxTemplateEngine engine;

  @Bean
  ThymeleafReactiveViewResolver thymeleafChunkedAndDataDrivenViewResolver() {
    final ThymeleafReactiveViewResolver viewResolver = new ThymeleafReactiveViewResolver();
    viewResolver.setTemplateEngine(engine);
    viewResolver.setOrder(1);
    viewResolver.setResponseMaxChunkSizeBytes(8192);
    return viewResolver;
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.viewResolver(thymeleafChunkedAndDataDrivenViewResolver());
  }
}

@Controller
@RequiredArgsConstructor
class IndexPage {

  final AppProps props;

  @GetMapping({"", "/", "/404"})
  Rendering index() {
    return Rendering
        .view("index")
        .modelAttribute("props", props)
        .build();
  }
}

@SpringBootApplication
@Import({ PropsAutoConfiguration.class})
public class MonolithicWebfluxWebApp {

  public static void main(String[] args) {
    SpringApplication.run(MonolithicWebfluxWebApp.class, args);
  }
}
