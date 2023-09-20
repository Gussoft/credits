package com.gussoft.credits.core.repository;

import com.gussoft.credits.core.models.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

  Mono<Credit> findByNumberAccount(String number);

  Flux<Credit> findByCustomer(String id);

}
