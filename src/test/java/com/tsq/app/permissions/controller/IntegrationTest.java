package com.tsq.app.permissions.controller;

import com.tsq.app.permissions.TsqPermissionsAppTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class IntegrationTest extends TsqPermissionsAppTest {

  private RestTemplate restTemplate = new RestTemplate();

  @LocalServerPort
  private int randomServerPort;

  public HttpEntity<String> getEntity(){
    HttpHeaders headers = new HttpHeaders();
    return new HttpEntity<>(headers);
  }

  public String getBaseUrl(){
    return "http://localhost:" + randomServerPort + "/permissoes?email=";
  }

  @Test
  public void invalidEmailTest() {

    List<String> emails = Arrays.asList("teste@test.cAm", "teste@", "@gmail.com");

    emails.forEach(email -> {
      try {
        restTemplate.exchange(
            getBaseUrl() + email,
            HttpMethod.GET,
            getEntity(),
            String.class);

      } catch (HttpClientErrorException e) {
        Assert.assertEquals(400, e.getStatusCode().value());
        Assert.assertEquals("Email inválido", e.getResponseBodyAsString());
      }

    });
  }

  @Test
  public void emailNotRegisteredTest() {
    String email = "teste@teste.com";

    try {
      restTemplate.exchange(
          getBaseUrl() + email,
          HttpMethod.GET,
          getEntity(),
          String.class);

    } catch (HttpServerErrorException e) {
      Assert.assertEquals(500, e.getStatusCode().value());
      Assert.assertEquals("Usuário não cadastrado", e.getResponseBodyAsString());
    }

  }

  @Test
  public void emailRegisteredTest() {
    List<String> emailsCadastrados = Arrays.asList(
        "rodrigo.soares@gmail.com",
        "maria.silva.sindica@gmail.com",
        "joao.costa@gmail.com"
    );

    List<String> expectedResults = Arrays.asList(
        "1;[(Reservas,Escrita), (Entregas,Nenhuma), (Usuarios,Leitura)]",
        "1;[(Reservas,Escrita), (Entregas,Nenhuma), (Usuarios,Escrita)]",
        "1;[(Reservas,Escrita), (Entregas,Nenhuma), (Usuarios,Escrita)]<br><br>"
            + "2;[(Reservas,Escrita), (Entregas,Leitura), (Usuarios,Escrita)]"
    );

    List<String> reponses = new ArrayList<>();

    emailsCadastrados.forEach(email -> {
      ResponseEntity<String> response = restTemplate.exchange(
          getBaseUrl() + email, HttpMethod.GET, getEntity(), String.class);

      Assert.assertEquals(200, response.getStatusCode().value());
      Assert.assertNotNull(response.getBody());
      reponses.add(response.getBody());

    });

    Assert.assertEquals(expectedResults, reponses);

  }

}