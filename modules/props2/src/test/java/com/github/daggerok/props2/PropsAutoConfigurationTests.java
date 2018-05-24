package com.github.daggerok.props2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = Props2AutoConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class PropsAutoConfigurationTests {

  @Test
  public void contextLoads() { }
}
