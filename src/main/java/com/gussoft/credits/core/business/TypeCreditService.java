package com.gussoft.credits.core.business;

import com.gussoft.credits.integration.transfer.request.TypeCreditRequest;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeCreditService {

  Flux<TypeCreditResponse> findAllType();

  Mono<TypeCreditResponse> findById(String id);

  Mono<TypeCreditResponse> findByName(String name);

  Mono<TypeCreditResponse> save(Mono<TypeCreditRequest> request);

  Mono<Void> delete(String id);

}
