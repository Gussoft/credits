package com.gussoft.credits.integration.mappers;

import com.gussoft.credits.core.models.Credit;
import com.gussoft.credits.core.models.TypeCredit;
import com.gussoft.credits.integration.transfer.request.CreditRequest;
import com.gussoft.credits.integration.transfer.response.CreditResponse;
import com.gussoft.credits.integration.transfer.response.TypeCreditResponse;
import org.springframework.beans.BeanUtils;

public class CreditMapper {

  public CreditMapper() {
  }

  public static Credit toCreditRequest(CreditRequest request) {
    Credit entity = new Credit();
    BeanUtils.copyProperties(request, entity);
    if (request.getType() != null) {
      entity.setType(new TypeCredit(request.getType().getId(), request.getType().getName()));
    }
    return entity;
  }

  public static CreditResponse toCreditResponse(Credit entity) {
    CreditResponse response = new CreditResponse();
    BeanUtils.copyProperties(entity, response);
    if (entity.getType() != null) {
      response.setType(new TypeCreditResponse(entity.getType().getId(), entity.getType().getName()));
    }
    return response;
  }

  public static Credit toCreditResponse2(CreditResponse response) {
    Credit account = new Credit();
    BeanUtils.copyProperties(response, account);
    if (response.getType() != null) {
      account.setType(new TypeCredit(response.getType().getId(), response.getType().getName()));
    }
    return account;
  }
}
