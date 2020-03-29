package com.tsq.app.permissions.layouts;

import com.tsq.app.permissions.model.PermissionResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PermissionReportLayout {

  public String simplifiedLayout(List<PermissionResponse> infoFillReport){

    StringBuilder builder = new StringBuilder();
    PermissionResponse lastInfo = infoFillReport.get(infoFillReport.size() - 1);

    infoFillReport.forEach(info -> {
      if(info.equals(lastInfo)){
        builder.append(info.getCondominiumId())
            .append(";")
            .append(customFormat(info.getAuthorizations()));
        return;
      }

      builder.append(info.getCondominiumId())
             .append(";")
             .append(customFormat(info.getAuthorizations()))
             .append("<br><br>");

    });

    return builder.toString();
  }

  private List<String> customFormat(List<Map<String, String>> data) {

    List<String> formatedAuthorizations = new ArrayList<>();

    for(Map<String, String> authorizations : data){

      for(Map.Entry<String, String> authorization : authorizations.entrySet()){

        StringBuilder builder = new StringBuilder();
        builder.append("(")
               .append(authorization.getKey())
               .append(",")
               .append(authorization.getValue())
               .append(")");

        formatedAuthorizations.add(builder.toString());

      }

    }

    return formatedAuthorizations;
  }

}
