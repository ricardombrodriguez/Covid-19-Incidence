package com.hw1.app.covid_service.connection;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class HttpClient {

    public String doHttpGet(String url) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.addHeader("X-RapidAPI-Key", "52450d0ccemsh0366c89f991686fp1cda94jsn99c2ccbeab83");
        CloseableHttpResponse response = client.execute(request);

        try {

            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);

        } finally {

            if (response != null) 
                response.close();
        }

    }

    
}
