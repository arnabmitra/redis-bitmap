package com.amitra.rediscount.service;

import com.amitra.rediscount.RedisBitMapApplication;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by amitra on 5/28/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { RedisBitMapApplication.class,
    CustomerServiceImplTest.MockConfig.class })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CustomerServiceImplTest {

  @Configuration static class MockConfig {

  }

  @Inject private CustomerService customerService;

  @Value("${local.server.port}") int port;

  @Before public void setUp() {
    RestAssured.port = port;
  }

  @BeforeClass public static void beforeClass() {

  }

  private final static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImplTest.class);

  @Test public void setCustomerAction() {
    for (int i = 10; i < 50; i++) {
      customerService.setUserAction("user:clickProduct", i);
    }
    for (int i = 10; i < 25; i++) {
      customerService.setUserAction("user:clickSearch", i);
    }
    customerService.setUserAction("user:clickProduct", 20000);

  }

  @Test public void getCustomerAction() {
    for (int i = 10; i < 50; i++) {
      boolean getUserAction = customerService.isUserAction("user:clickProduct", Long.valueOf(i));
      assertThat(getUserAction).isEqualTo(true);
    }
    long numberOfUserActions = customerService.getNumberOfUserClicksAllAction("user:clickProduct", "user:clickSearch");
    assertThat(numberOfUserActions).isEqualTo(15);
    boolean getUserAction = customerService.isUserAction("user:clickProduct", 51110l);
    assertThat(getUserAction).isEqualTo(false);
  }
}
