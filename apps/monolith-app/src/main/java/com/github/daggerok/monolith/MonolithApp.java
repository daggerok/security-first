package com.github.daggerok.monolith;

import com.github.daggerok.props.PropsAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
    PropsAutoConfiguration.class,
})
@SpringBootApplication
public class MonolithApp {

  public static void main(String[] args) {
    SpringApplication.run(MonolithApp.class, args);
  }
}
