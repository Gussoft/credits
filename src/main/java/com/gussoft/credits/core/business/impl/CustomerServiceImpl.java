package com.gussoft.credits.core.business.impl;

import com.gussoft.credits.core.business.CustomerService;
import com.gussoft.credits.core.models.dto.Customer;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private WebClient.Builder client;

  @Override
  public Mono<Customer> findById(String id) {
    return client.build().get()
      .uri("/{id}", Collections.singletonMap("id", id))
      .accept(MediaType.APPLICATION_JSON)
      .exchangeToMono(response -> response.bodyToMono(Customer.class));
  }

}
