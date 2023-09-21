package com.gussoft.credits.core.business.impl;

import com.gussoft.credits.core.business.CreditService;
import com.gussoft.credits.core.business.CustomerService;
import com.gussoft.credits.core.models.Credit;
import com.gussoft.credits.core.models.TypeCredit;
import com.gussoft.credits.core.models.dto.CreditDetail;
import com.gussoft.credits.core.repository.CreditRepository;
import com.gussoft.credits.integration.mappers.CreditMapper;
import com.gussoft.credits.integration.transfer.request.CreditRequest;
import com.gussoft.credits.integration.transfer.request.RegistryRequest;
import com.gussoft.credits.integration.transfer.response.CreditResponse;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreditServiceImpl implements CreditService {

  @Autowired
  private CreditRepository repo;

  @Autowired
  private CustomerService customerService;

  @Override
  public Flux<CreditResponse> findAll() {
    return repo.findAll().map(CreditMapper::toCreditResponse);
  }

  @Override
  public Mono<CreditResponse> findById(String id) {
    return repo.findById(id).map(CreditMapper::toCreditResponse)
      .switchIfEmpty(Mono.error(new RuntimeException("Id No encontrado : " + id)));
  }

  @Override
  public Mono<Map<String, Object>> findByCustomerAccount(String id) {
    Map<String, Object> map = new HashMap<>();
    return customerService.findById(id)
      .flatMap(customer -> {
        map.put("Customer", customer);
        return repo.findByCustomer(customer.getId())
          .map(acc -> acc)
          .distinct()
          .collectList()
          .flatMap(credits -> {
            for (Credit cr : credits) {
              map.put("Credits ".concat(cr.getType().getName()), cr);
            }
            return Mono.just(map);
          });
      });
  }

  @Override
  public Mono<CreditResponse> save(Mono<CreditRequest> request, String typeC) {
    return request.map(CreditMapper::toCreditRequest)
      .flatMap(credit -> {
        return validateCustomerWithCreditType(credit.getCustomer(), typeC, credit.getType().getName())
          .flatMap(isValid -> {
            if (isValid) {
              if(credit.getType().getName().equalsIgnoreCase("EMPRESARIAL")) {
                credit.setCreateAt(new Date());
                return repo.save(credit);
              } else if(credit.getType().getName().equalsIgnoreCase("TARJETA EMPRESARIAL")) {
                credit.setCreateAt(new Date());
                return repo.save(credit);
              } else if(credit.getType().getName().equalsIgnoreCase("TARJETA PERSONAL")) {
                credit.setCreateAt(new Date());
                return repo.save(credit);
              } else {
                credit.setCreateAt(new Date());
                log.info("credito personal " + credit.getCustomer());
                return repo.save(credit);
              }
            }
            return Mono.error(new RuntimeException("Ya cuenta con credito Registrado"));
          });
      }).map(CreditMapper::toCreditResponse);
  }

  @Override
  public Mono<CreditResponse> cardPayment(String id, BigDecimal pay) {
    return repo.findById(id)
      .flatMap(credit -> {
        credit.setConsume(credit.getConsume().subtract(pay));
        log.info("Pago Realizado con exito " + pay);
        return repo.save(credit);
      }).map(CreditMapper::toCreditResponse);
  }

  @Override
  public Mono<CreditResponse> cardConsumer(String id, BigDecimal pay) {
    return repo.findById(id)
      .flatMap(credit -> {
          if (credit.getConsume().add(pay).compareTo(credit.getAmount()) <= 0) {
            credit.setConsume(credit.getConsume().add(pay));
            log.info("Consumo Realizado con exito " + pay);
            return repo.save(credit);
          }
          return Mono.error(new RuntimeException("Consumo de : " + pay + " Excede el limite de Credito!"));
      }).map(CreditMapper::toCreditResponse);
  }


  @Override
  public Mono<CreditResponse> addCustomerToAccount(RegistryRequest request, String typeC, String typeA) {
    return validateCustomerWithCreditType(request.getCustomer(), typeC, "")
      .flatMap(isValid -> {
        return repo.findById(request.getCustomer())
          .map(CreditMapper::toCreditResponse)
          .map(CreditMapper::toCreditResponse2)
          .flatMap(credit -> {
            if (typeC.equalsIgnoreCase("PERSONAL")) {
              credit.setCustomer(request.getCustomer());
              return repo.save(credit);
            }

            return Mono.just(credit);
          }).map(CreditMapper::toCreditResponse);
      });
  }

  @Override
  public Mono<Void> delete(String id) {
    return repo.deleteById(id);
  }

  private Mono<Boolean> validateCustomerWithCreditType(String id, String types, String typeCredit) {
    return customerService.findById(id)
      .flatMap(customer -> {
        return repo.findByCustomer(customer.getId())
          .map(Credit::getType)
          .distinct()
          .collectList()
          .flatMap(type -> {
            if (customer.getType().getName().equalsIgnoreCase("PERSONAL")) {
              List<TypeCredit> credits = type.stream()
                .filter(c -> c.getName().equalsIgnoreCase(typeCredit))
                .collect(Collectors.toList());

              if (!credits.isEmpty()) {
                log.info("Ya cuenta con esta Credito, Rechazado");
                return Mono.just(false);
              } else {
                log.info("No cuenta con esta Credito, Aprobado");
                return Mono.just(true);
              }
            }
            log.info("Cliente Empresarial, Solo Creditos Empresariales");
            return Mono.just(true);
          });
      }).defaultIfEmpty(true);
  }

  @Override
  public Mono<CreditResponse> calculateCredit(CreditRequest request, String idCustomer) {
    double monthlyInterestRate = request.getBenefit().doubleValue() / 12 / 100;
    BigDecimal monthlyPayment = calculateMonthlyPayment(request.getAmount(), monthlyInterestRate, request.getDay());

    BigDecimal insuranceCost = request.getSure().divide(BigDecimal.valueOf(request.getDay()), 2, RoundingMode.HALF_UP);

    List<CreditDetail> details = new ArrayList<>();
    BigDecimal remainingBalance = request.getAmount();
    LocalDate paymentDate = LocalDate.now().withDayOfMonth(request.getDay());
    for (int month = 1; month <= request.getNumber(); month++) {
      YearMonth nextPaymentMonth = YearMonth.from(paymentDate).plusMonths(1);

      BigDecimal interest = remainingBalance.multiply(BigDecimal.valueOf(monthlyInterestRate));
      BigDecimal insurancePayment = insuranceCost
        .multiply(BigDecimal.valueOf(remainingBalance.doubleValue() / request.getAmount().doubleValue()))
        .setScale(2, RoundingMode.HALF_UP);
      BigDecimal payment = monthlyPayment.subtract(interest).subtract(insurancePayment);

      interest = interest.setScale(2, RoundingMode.HALF_UP);
      payment = payment.setScale(2, RoundingMode.HALF_UP);
      monthlyPayment = monthlyPayment.setScale(2, RoundingMode.HALF_UP);

      CreditDetail creditDetail = new CreditDetail(month, paymentDate, payment, interest,
        insurancePayment, monthlyPayment, null,"PENDIENTE");
      details.add(creditDetail);

      remainingBalance = remainingBalance.subtract(payment);
      paymentDate = nextPaymentMonth.atDay(request.getDay());
    }
    BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(request.getDay())).add(request.getSure());
    CreditResponse credit = new CreditResponse();
    credit.setCustomer(request.getCustomer());
    credit.setCreateAt(new Date());
    credit.setAmount(request.getAmount());
    credit.setBenefit(totalAmount.subtract(request.getAmount()));
    credit.setDetail(details);
    credit.setType(new TypeCreditResponse(request.getType().getId(), request.getType().getName()));
    credit.setDay(request.getDay());
    return Mono.just(credit);
  }

  private BigDecimal calculateMonthlyPayment(BigDecimal loan, double interest, int numberOfMonths) {
    return loan.multiply(BigDecimal.valueOf(interest))
      .divide(BigDecimal.valueOf(1 - Math.pow(1 + interest, -numberOfMonths)), 2, BigDecimal.ROUND_HALF_EVEN);
  }

}
