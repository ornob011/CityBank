package com.example.phpreplica;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

@Service
public class RequestService {

    private final CloseableHttpClient httpClient;

    @Autowired
    public RequestService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String sendPostRequest(String url, String payload) {
        HttpPost httpPost = new HttpPost(url);

        // Add headers and body
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException | ParseException e) {
            String error = "Error1: " + e.getMessage();
            System.out.println(error);
            return "error";
        }
    }
}

