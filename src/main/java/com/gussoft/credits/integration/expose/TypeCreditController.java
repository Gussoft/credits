package com.gussoft.credits.integration.expose;

import com.gussoft.credits.core.business.TypeCreditService;
import com.gussoft.credits.integration.transfer.request.TypeCreditRequest;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TypeCreditController {

  @Autowired
  private TypeCreditService service;

  @GetMapping("/credits/types")
  public Mono<ResponseEntity<Flux<TypeCreditResponse>>> findAllCustomer() {
    return Mono.just(
      ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.findAllType()));
  }

  @GetMapping("/credits/types/{id}")
  public Mono<ResponseEntity<TypeCreditResponse>> findById(@PathVariable String id) {
    return service.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping(path = "/credits/types")
  public Mono<ResponseEntity<TypeCreditResponse>> create(@RequestBody Mono<TypeCreditRequest> request) {
    return request.flatMap(account -> {
      return service.save(Mono.just(account)).map(types -> ResponseEntity
        .created(URI.create("/api/v1/customers/types/".concat(types.getId()))).body(types));
    }).onErrorResume(throwable -> {
      return Mono.just(throwable).cast(WebExchangeBindException.class)
        .flatMap(e -> Mono.just(e.getFieldErrors()))
        .flatMapMany(Flux::fromIterable)
        .map(field -> "El campo ".concat(field.getField()).concat(" ").concat(
          Objects.requireNonNull(field.getDefaultMessage())))
        .collectList()
        .flatMap(list -> {
          log.error(list.toString());
          return Mono.just(ResponseEntity.badRequest().build());
        });
    });
  }

  @DeleteMapping("/credits/types/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return service.delete(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
  }

}
