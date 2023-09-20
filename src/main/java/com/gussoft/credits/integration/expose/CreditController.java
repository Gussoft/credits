package com.gussoft.credits.integration.expose;

import com.gussoft.credits.core.business.CreditService;
import com.gussoft.credits.integration.mappers.CreditMapper;
import com.gussoft.credits.integration.transfer.request.CardRequest;
import com.gussoft.credits.integration.transfer.request.CreditRequest;
import com.gussoft.credits.integration.transfer.response.CreditResponse;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CreditController {

  @Autowired
  private CreditService service;

  @GetMapping("/credits")
  public Mono<ResponseEntity<Flux<CreditResponse>>> findAll() {
    return Mono.just(
      ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.findAll()));
  }

  @GetMapping("/credits/{id}")
  public Mono<ResponseEntity<CreditResponse>> findById(@PathVariable String id) {
    return service.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build())
      .doOnError(Throwable::getMessage);
  }

  @GetMapping("/credits/customer/{id}/home")
  public Mono<ResponseEntity<Map<String,Object>>> findByCustomerPanel(@PathVariable String id) {
    return service.findByCustomerAccount(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping(path = "/credits/simulation")
  public Mono<ResponseEntity<CreditResponse>> createSimulations(@RequestBody Mono<CreditRequest> request) {
    return request.flatMap(credit -> {
      return service.calculateCredit(credit, null).map(ResponseEntity::ok);
    });
  }

  @PostMapping("/credits")
  public Mono<ResponseEntity<CreditResponse>> registryAccountsCustomer(
    @RequestParam("typeCustomer") String typeC,
    @RequestBody CreditRequest request) {
    return service.save(Mono.just(request), typeC)
      .map(response -> {
      log.info(response.getId());
      return ResponseEntity.ok(response);
    }).doOnError(Throwable::getMessage);
  }

  @PutMapping("/credits/card/{id}/consumer")
  public Mono<ResponseEntity<CreditResponse>> updateConsumer(
    @PathVariable String id, @RequestBody CardRequest card) {
    return service.cardConsumer(id, card.getAmount())
      .map(ResponseEntity::ok)
      .doOnError(Throwable::getMessage);
  }

  @PutMapping("/credits/card/{id}/payment")
  public Mono<ResponseEntity<CreditResponse>> updatePayment(
    @PathVariable String id, @RequestBody CardRequest card) {
    return service.cardPayment(id, card.getAmount())
      .map(ResponseEntity::ok)
      .doOnError(Throwable::getMessage);
  }

}
