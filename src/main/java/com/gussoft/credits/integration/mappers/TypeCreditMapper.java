package com.gussoft.credits.integration.mappers;

import com.gussoft.credits.core.models.TypeCredit;
import com.gussoft.credits.integration.transfer.request.TypeCreditRequest;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import org.springframework.beans.BeanUtils;

public class TypeCreditMapper {

  public TypeCreditMapper() {
  }

  public static TypeCredit toTypeCreditRequest(TypeCreditRequest request) {
    TypeCredit response = new TypeCredit();
    BeanUtils.copyProperties(request, response);
    return response;
  }

  public static TypeCreditResponse toTypeCreditResponse(TypeCredit request) {
    TypeCreditResponse response = new TypeCreditResponse();
    BeanUtils.copyProperties(request, response);
    return response;
  }

}
