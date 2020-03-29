package com.tsq.app.permissions.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsq.app.permissions.exception.ApplicationException;
import com.tsq.app.permissions.service.PermissionService;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;

@Slf4j
public class DataHelper {

  public static List<String[]> readDataFile() throws IOException {

    List<String[]> data = new ArrayList<>();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    InputStream input = PermissionService.class.getResourceAsStream("/database.txt");
    IOUtils.copy(input, outputStream);

    BufferedReader reader = new BufferedReader(
        new StringReader(new String(outputStream.toByteArray())));

    String line = reader.readLine();

    while (line != null){
      data.add(line.split(";"));
      line = reader.readLine();
    }

    reader.close();

    return data;

  }

  public static String[] jsonToStringArray(String data) {
    String[] dataArray;
    String dataWithoutParentheses = data.replaceAll("[()]", "\"");

    try {
      dataArray = new ObjectMapper().readValue(dataWithoutParentheses, String[].class);

    } catch (IOException e){
      log.error("Falha ao realizar o parse de Json para String[]", e);
      throw new ApplicationException("Falha ao realizar o parse de Json para String[]");
    }

    return dataArray;
  }

  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

}
