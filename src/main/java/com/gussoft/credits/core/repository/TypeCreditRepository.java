package com.gussoft.credits.core.repository;

import com.gussoft.credits.core.models.TypeCredit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TypeCreditRepository extends ReactiveMongoRepository<TypeCredit, String> {

  Mono<TypeCredit> findByName(String name);

}
