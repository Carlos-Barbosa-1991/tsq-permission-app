package com.tsq.app.permissions.builder;

import com.tsq.app.permissions.exception.ApplicationException;
import com.tsq.app.permissions.helper.DataHelper;
import com.tsq.app.permissions.model.Group;
import com.tsq.app.permissions.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@Data
public class ConsolidatedDataBase {

  private List<User> users;
  private List<Group> groups;

  public static User getUserData(final String[] userData) {
    List<Group> groups = new ArrayList<>();
    List<String[]> formattedData = new ArrayList<>();

    formattedData.add(DataHelper.jsonToStringArray(userData[2]));

    for(String data : formattedData.get(0)){
      String[] groupData = data.split(",");

      Group group = Group.builder()
          .groupType(groupData[0])
          .condominiumId(Integer.valueOf(groupData[1]))
          .build();

      groups.add(group);
    }

    return User.builder()
        .email(userData[1])
        .groups(groups)
        .build();
  }

  public static Group getGroupData(final String[] groupData) throws ApplicationException {
    List<Map<String, String>> authorizations = new ArrayList<>();
    List<String[]> formattedData = new ArrayList<>();

    formattedData.add(DataHelper.jsonToStringArray(groupData[3]));

    for(String data : formattedData.get(0)){
      Map<String, String> authorization = new HashMap<>();

      String[] authorizationData = data.split(",");

      authorization.put(authorizationData[0], authorizationData[1]);

      authorizations.add(authorization);
    }

    return Group.builder()
        .groupType(groupData[1])
        .condominiumId(Integer.valueOf(groupData[2]))
        .authorizations(authorizations)
        .build();
  }

}
