package com.tsq.app.permissions.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Group {

  private String groupType;
  private Integer condominiumId;
  private List<Map<String, String>> authorizations;


}
