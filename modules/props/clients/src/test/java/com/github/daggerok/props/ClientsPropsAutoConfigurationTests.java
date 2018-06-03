package com.github.daggerok.props;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ClientsPropsAutoConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class ClientsPropsAutoConfigurationTests {

  @Test
  public void contextLoads() { }
}
