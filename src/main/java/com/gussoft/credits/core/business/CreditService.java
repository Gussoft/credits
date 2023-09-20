package com.gussoft.credits.core.business;

import com.gussoft.credits.integration.transfer.request.CreditRequest;
import com.gussoft.credits.integration.transfer.request.RegistryRequest;
import com.gussoft.credits.integration.transfer.response.CreditResponse;
import java.math.BigDecimal;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {

  Flux<CreditResponse> findAll();

  Mono<CreditResponse> findById(String id);

  Mono<Map<String,Object>> findByCustomerAccount(String id);

  Mono<CreditResponse> save(Mono<CreditRequest> request, String typeC);

  Mono<CreditResponse> cardPayment(String id, BigDecimal pay);

  Mono<CreditResponse> cardConsumer(String id, BigDecimal pay);
  Mono<CreditResponse> addCustomerToAccount(RegistryRequest request, String typeC, String typeA);

  Mono<Void> delete(String id);

  Mono<CreditResponse> calculateCredit(CreditRequest request, String idCustomer);

}
