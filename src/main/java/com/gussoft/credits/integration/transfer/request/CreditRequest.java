package com.gussoft.credits.integration.transfer.request;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditRequest {

  private String customer;
  private String numberAccount;
  private TypeCreditRequest type;
  private Date createAt;
  private BigDecimal amount;
  private BigDecimal benefit;
  private Integer day;
  private Integer number;
  private BigDecimal sure;
  private BigDecimal consume;

}
