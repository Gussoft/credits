package com.gussoft.credits.integration.transfer.response;

import com.gussoft.credits.core.models.dto.CreditDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
public class CreditResponse {

  private String id;

  private String customer;
  private String numberAccount;
  private TypeCreditResponse type;
  private Date createAt;
  private BigDecimal amount;
  private BigDecimal benefit;
  private Integer day;
  private BigDecimal consume;
  private List<CreditDetail> detail;

}
