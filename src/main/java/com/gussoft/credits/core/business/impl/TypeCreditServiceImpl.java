package com.gussoft.credits.core.business.impl;

import com.gussoft.credits.core.business.TypeCreditService;
import com.gussoft.credits.core.repository.TypeCreditRepository;
import com.gussoft.credits.integration.mappers.TypeCreditMapper;
import com.gussoft.credits.integration.transfer.request.TypeCreditRequest;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeCreditServiceImpl implements TypeCreditService {

  @Autowired
  private TypeCreditRepository repo;

  @Override
  public Flux<TypeCreditResponse> findAllType() {
    return repo.findAll().map(TypeCreditMapper::toTypeCreditResponse);
  }

  @Override
  public Mono<TypeCreditResponse> findById(String id) {
    return repo.findById(id).map(TypeCreditMapper::toTypeCreditResponse)
      .switchIfEmpty(Mono.error(new RuntimeException("Id No encontrado")));
  }

  @Override
  public Mono<TypeCreditResponse> findByName(String name) {
    return repo.findByName(name).map(TypeCreditMapper::toTypeCreditResponse);
  }

  @Override
  public Mono<TypeCreditResponse> save(Mono<TypeCreditRequest> request) {
    return request.map(TypeCreditMapper::toTypeCreditRequest)
      .flatMap(repo::save)
      .map(TypeCreditMapper::toTypeCreditResponse);
  }

  @Override
  public Mono<Void> delete(String id) {
    return repo.deleteById(id);
  }

}
