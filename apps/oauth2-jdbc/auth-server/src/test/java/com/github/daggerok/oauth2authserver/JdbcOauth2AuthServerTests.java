package com.github.daggerok.oauth2authserver;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = JdbcOauth2AuthServer.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JdbcOauth2AuthServerTests {

  @Test
  public void contextLoads() {
  }
}
