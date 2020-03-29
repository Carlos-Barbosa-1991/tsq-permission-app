package com.tsq.app.permissions.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

  private String email;
  private List<Group> groups;
}
