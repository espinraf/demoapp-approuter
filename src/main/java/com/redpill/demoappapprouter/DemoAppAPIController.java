package com.redpill.demoappapprouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DemoAppAPIController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/demo/hello")
    public String index() {
        logger.info("Receiving a HTTP Request");
        return "Greetings from Spring Boot Docker Container!";
    }

    @PostConstruct
    public void registerConsul(){

        InetAddress ip = null;
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

        } catch (UnknownHostException e) {

            e.printStackTrace();

        }

        RestTemplate restTemplate =  new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);

        //final String baseUrl = "http://approuter_discovery_1:8500/v1/agent/service/register";
        final String baseUrl = "http://localhost:8500/v1/agent/service/register";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonString = "{\n" +
                "  \"ID\": \"demo\",\n" +
                "  \"Name\": \"demo\",\n" +
                "  \"Tags\": [\n" +
                "    \"primary\",\n" +
                "    \"v1\"\n" +
                "  ],\n" +
                "  \"Address\": \"" + ip.getHostAddress()  +  "\",\n" +
                "  \"Port\": 8080,\n" +
                "  \"Meta\": {\n" +
                "    \"mule_version\": \"3.0\"\n" +
                "  },\n" +
                "  \"EnableTagOverride\": false\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<String>(jsonString, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
    }

}