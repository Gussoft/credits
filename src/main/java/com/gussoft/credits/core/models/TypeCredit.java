package com.gussoft.credits.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "type_credit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeCredit {

  private String id;

  private String name;

}
