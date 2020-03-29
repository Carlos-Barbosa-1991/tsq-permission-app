package com.tsq.app.permissions.enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DataType {
  USER("Usuario"),
  GROUP("Grupo");
  
  @Getter
  String value;

}
