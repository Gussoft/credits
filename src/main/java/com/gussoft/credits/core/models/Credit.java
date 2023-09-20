package com.gussoft.credits.core.models;

import com.gussoft.credits.core.models.dto.CreditDetail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
  @Id
  @NotEmpty
  private String id;

  private String customer;
  @NotBlank
  private String numberAccount;
  private TypeCredit type;
  private Date createAt;
  private BigDecimal amount;
  private BigDecimal benefit;
  private Integer day;
  private BigDecimal consume;
  private List<CreditDetail> detail;

}
