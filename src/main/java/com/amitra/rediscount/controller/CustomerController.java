package com.amitra.rediscount.controller;

import com.amitra.rediscount.model.ActionEvent;
import com.amitra.rediscount.service.CustomerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping(value="/customer")
public class CustomerController {

  @Inject CustomerService customerService;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void post( @RequestBody ActionEvent actionEvent) throws ExecutionException, InterruptedException {
    customerService.setUserAction("userAction:"+actionEvent.getEventType(),actionEvent.getCustomerId());
  }
}
