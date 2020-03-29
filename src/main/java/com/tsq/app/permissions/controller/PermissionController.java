package com.tsq.app.permissions.controller;

import com.tsq.app.permissions.helper.EmailHelper;
import com.tsq.app.permissions.layouts.PermissionReportLayout;
import com.tsq.app.permissions.model.PermissionResponse;
import com.tsq.app.permissions.service.PermissionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PermissionController {

  private PermissionService permissionService;
  private PermissionReportLayout permissionReportLayout;

  public PermissionController(PermissionService permissionService,
      PermissionReportLayout permissionReportLayout) {
    this.permissionService = permissionService;
    this.permissionReportLayout = permissionReportLayout;
  }

  @GetMapping("permissoes")
  public ResponseEntity<Object> searchPermissions(@RequestParam("email") String email)
      throws Exception {

    if (!EmailHelper.isValidEmail(email)) {
      return new ResponseEntity<>("Email inválido", HttpStatus.BAD_REQUEST);
    }

    try {
      List<PermissionResponse> response = permissionService.searchPermissionsByEmail(email);

      if (response.isEmpty()) {
        return new ResponseEntity<>("Não foram encontradas permissões para o email " + email,
            HttpStatus.NOT_FOUND);
      }

      String populatedReportLayout = permissionReportLayout
          .simplifiedLayout(response);

      return new ResponseEntity<>(populatedReportLayout, HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

}
