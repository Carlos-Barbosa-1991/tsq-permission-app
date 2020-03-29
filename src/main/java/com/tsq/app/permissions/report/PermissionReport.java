package com.tsq.app.permissions.report;

import com.tsq.app.permissions.builder.ConsolidatedDataBase;
import com.tsq.app.permissions.exception.ApplicationException;
import com.tsq.app.permissions.exception.BusinessException;
import com.tsq.app.permissions.helper.DataHelper;
import com.tsq.app.permissions.model.Group;
import com.tsq.app.permissions.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PermissionReport {

  public List<Group> higherPermissions(final String email,
      final ConsolidatedDataBase consolidatedDataBase) throws BusinessException {

    List<User> dataBaseUsers = consolidatedDataBase.getUsers();
    List<Group> dataBaseGroups = consolidatedDataBase.getGroups();

    Optional<User> registeredUser = dataBaseUsers.stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst();

    if (!registeredUser.isPresent()) {
      throw new BusinessException("Usuário não cadastrado");
    }

    List<Group> userGroups = registeredUser.get().getGroups();

    List<String> userGroupType = userGroups.stream()
        .map(Group::getGroupType)
        .collect(Collectors.toList());

    List<Group> userGroupDetails = dataBaseGroups.stream()
        .filter(group -> userGroupType.contains(group.getGroupType()))
        .collect(Collectors.toList());

    List<Group> higherPermissionGroups = new ArrayList<>();

    List<Group> higherAuthorizations = findHigherAuthorizations(userGroupDetails);

    if (!higherAuthorizations.isEmpty()) {

      higherAuthorizations.forEach( higherAuthorization -> {

      Group higherPermissionGroup = Group.builder()
          .groupType(higherAuthorization.getGroupType())
          .condominiumId(higherAuthorization.getCondominiumId())
          .authorizations(higherAuthorization.getAuthorizations())
          .build();

      higherPermissionGroups.add(higherPermissionGroup);

      });

    }

    return higherPermissionGroups;

  }

  private List<Group> findHigherAuthorizations(List<Group> userGroupDetails) {

    List<Group> updatedUserGroupDetails = new ArrayList<>();
    List<String> functionalityTypes = Arrays.asList("Reservas", "Entregas", "Usuarios");
    List<String> permissions = Arrays.asList("Escrita", "Leitura", "Nenhuma");

    userGroupDetails.forEach(detail -> {

      List<Map<String, String>> higherAutorizations = new ArrayList<>();
      higherAutorizations.add(new HashMap<>());
      higherAutorizations.add(new HashMap<>());
      higherAutorizations.add(new HashMap<>());

      List<List<Map<String, String>>> authorizationsByCondominium = userGroupDetails.stream()
          .filter(info -> info.getCondominiumId().equals(detail.getCondominiumId()))
          .map(Group::getAuthorizations)
          .collect(Collectors.toList());

      authorizationsByCondominium.forEach(auths -> {

        auths.forEach(auth -> {

          for (String functionality : functionalityTypes) {

            for (String permission : permissions) {
              Optional<Entry<String, String>> higherPermission = auth.entrySet().stream()
                  .filter(entry -> entry.getKey().equals(functionality))
                  .filter(entry -> entry.getValue().equals(permission))
                  .findFirst();

              if (higherPermission.isPresent()) {

                switch (functionality){
                  case "Reservas":
                    if(higherPermission.get().getValue().equals("Escrita")
                        || higherPermission.get().getValue().equals("Leitura")
                        || higherPermission.get().getValue().equals("Nenhuma")){

                      if (!higherAutorizations.get(0).containsValue("Escrita")) {
                        higherAutorizations.set(0, auth);
                        return;
                      }

                      if(!higherAutorizations.get(0).containsValue("Leitura")
                          && !higherAutorizations.get(0).containsValue("Escrita")) {
                        higherAutorizations.set(0, auth);
                        return;
                      }

                    }
                    break;
                  case "Entregas":
                    if(higherPermission.get().getValue().equals("Escrita")
                        || higherPermission.get().getValue().equals("Leitura")
                        || higherPermission.get().getValue().equals("Nenhuma")){

                      if (!higherAutorizations.get(1).containsValue("Escrita")) {
                        higherAutorizations.set(1, auth);
                        return;
                      }

                      if(!higherAutorizations.get(1).containsValue("Leitura")
                          && !higherAutorizations.get(1).containsValue("Escrita")) {
                        higherAutorizations.set(1, auth);
                        return;
                      }

                    }
                    break;
                  case "Usuarios":
                    if(higherPermission.get().getValue().equals("Escrita")
                        || higherPermission.get().getValue().equals("Leitura")
                        || higherPermission.get().getValue().equals("Nenhuma")){

                      if (!higherAutorizations.get(2).containsValue("Escrita")) {
                        higherAutorizations.set(2, auth);
                        return;
                      }

                      if(!higherAutorizations.get(2).containsValue("Leitura")
                          && !higherAutorizations.get(2).containsValue("Escrita")) {
                        higherAutorizations.set(2, auth);
                        return;
                      }

                    }
                    break;
                  default:
                    throw new ApplicationException("Funcionalidade não implementada");

                }

              }
            }
          }
        });


      });

      Group group = Group.builder()
          .groupType(detail.getGroupType())
          .condominiumId(detail.getCondominiumId())
          .authorizations(higherAutorizations)
          .build();

      updatedUserGroupDetails.add(group);

    });

    return updatedUserGroupDetails.stream()
        .filter(DataHelper.distinctByKey(Group::getCondominiumId))
        .collect(Collectors.toList());
  }
}
