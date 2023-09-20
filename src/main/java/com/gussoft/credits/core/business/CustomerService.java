package com.gussoft.credits.core.business;

import com.gussoft.credits.core.models.dto.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {

  Mono<Customer> findById(String id);

}
