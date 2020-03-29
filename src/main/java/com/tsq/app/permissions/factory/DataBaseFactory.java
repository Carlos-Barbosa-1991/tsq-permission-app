package com.tsq.app.permissions.factory;

import static com.tsq.app.permissions.enumerated.DataType.GROUP;
import static com.tsq.app.permissions.enumerated.DataType.USER;

import com.tsq.app.permissions.builder.ConsolidatedDataBase;
import com.tsq.app.permissions.exception.ApplicationException;
import com.tsq.app.permissions.model.Group;
import com.tsq.app.permissions.model.User;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DataBaseFactory {

  public ConsolidatedDataBase getData(final List<String[]> dataBase) {

    List<User> users = new ArrayList<>();
    List<Group> groups = new ArrayList<>();

    dataBase.forEach(data -> {

      if (data[0].equals(USER.getValue())) {
        users.add(ConsolidatedDataBase.getUserData(data));

      } else if (data[0].equals(GROUP.getValue())) {
        groups.add(ConsolidatedDataBase.getGroupData(data));

      } else {
        throw new ApplicationException("Arquivo fora do layout!");
      }

    });

    return ConsolidatedDataBase.builder()
        .users(users)
        .groups(groups)
        .build();

  }

}
