package com.tsq.app.permissions.service;

import com.tsq.app.permissions.builder.ConsolidatedDataBase;
import com.tsq.app.permissions.exception.BusinessException;
import com.tsq.app.permissions.factory.DataBaseFactory;
import com.tsq.app.permissions.helper.DataHelper;
import com.tsq.app.permissions.model.Group;
import com.tsq.app.permissions.model.PermissionResponse;
import com.tsq.app.permissions.report.PermissionReport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  private DataBaseFactory dataBaseFactory;
  private PermissionReport permissionReport;

  public PermissionService(
      DataBaseFactory dataBaseFactory,
      PermissionReport permissionReport) {
    this.dataBaseFactory = dataBaseFactory;
    this.permissionReport = permissionReport;
  }

  public List<PermissionResponse> searchPermissionsByEmail(final String email)
      throws IOException, BusinessException {

    List<PermissionResponse> response = new ArrayList<>();

    List<String[]> dataBase = DataHelper.readDataFile();
    ConsolidatedDataBase consolidatedData = dataBaseFactory.getData(dataBase);

    List<Group> higherPermissionsReport = permissionReport
        .higherPermissions(email, consolidatedData);

    higherPermissionsReport.forEach(permissionReport ->

        response.add(PermissionResponse.builder()
            .condominiumId(permissionReport.getCondominiumId())
            .authorizations(permissionReport.getAuthorizations())
            .build()

    ));

    return response;

  }



}
