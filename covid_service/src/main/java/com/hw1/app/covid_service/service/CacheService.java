package com.hw1.app.covid_service.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.Request;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private Cache cache;

    public List<Request> getCache() throws URISyntaxException, IOException, ParseException {

        return cache.getCache();
    }
    
}
