package com.gussoft.credits.core.models.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class CreditDetail {

  private int month;
  private LocalDate payment;
  private BigDecimal loan;
  private BigDecimal interest;
  private BigDecimal sure;
  private BigDecimal total;
  private LocalDate datePay;
  private String status;

}
