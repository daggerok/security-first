package com.github.daggerok.monolith;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = MonolithicMvcWebApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class MonolithicMvcWebAppTests {

  @Test
  public void contextLoads() {
  }
}
