package com.springboot.rest.service;

import com.springboot.rest.dto.MemberDto;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;


@Service
public class RestTemplateService {

    public String getName() {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9090")
            .path("/api/v1/crud-api")
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public String getNameWithPathVariable() {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9090")
            .path("/api/v1/crud-api/{name}")
            .encode()
            .build()
            .expand("Dodam") // 복수의 값을 넣어야할 경우 , 를 추가하여 구분
            .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public String getNameWithParameter() {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9090")
            .path("/api/v1/crud-api/param")
            .queryParam("name", "Dodam")
            .encode()
            .build()
            .toUri();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        return responseEntity.getBody();
    }

    public ResponseEntity<MemberDto> postWithParamAndBody() {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9090")
            .path("/api/v1/crud-api")
            .queryParam("name", "Dodam")
            .queryParam("email", "dodam@naver.com")
            .queryParam("organization", "corner")
            .encode()
            .build()
            .toUri();

        MemberDto memberDto = new MemberDto();
        memberDto.setName("dodam!!");
        memberDto.setEmail("dodam@gmail.com");
        memberDto.setOrganization("duksung");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDto> responseEntity = restTemplate.postForEntity(uri, memberDto,
            MemberDto.class);

        return responseEntity;
    }

    public ResponseEntity<MemberDto> postWithHeader() {
        URI uri = UriComponentsBuilder
            .fromUriString("http://localhost:9090")
            .path("/api/v1/crud-api/add-header")
            .encode()
            .build()
            .toUri();

        MemberDto memberDTO = new MemberDto();
        memberDTO.setName("dodam");
        memberDTO.setEmail("dodam@naver.com");
        memberDTO.setOrganization("Corner");

        RequestEntity<MemberDto> requestEntity = RequestEntity
            .post(uri)
            .header("my-header", "Corner API")
            .body(memberDTO);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MemberDto> responseEntity = restTemplate.exchange(requestEntity,
            MemberDto.class);

        return responseEntity;
    }


    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        HttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(500)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setMaxConnTotal(500)
                .setMaxConnPerRoute(500)
                .build();

        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);

        return restTemplate;
    }

}
