package com.hw1.app.covid_service.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.service.CacheService;
import com.hw1.app.covid_service.service.CovidService;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CovidController {

    @Autowired
    private CovidService covidService;

    @Autowired
    private CacheService cacheService;

    @GetMapping("/countries")
    public List<String> getAllCountries() throws URISyntaxException, IOException, ParseException {
        return covidService.getAllCountries();
    }

    @GetMapping("/interval_history")
    public Request getCountryHistory(@RequestParam String country, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date initial, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date end) throws URISyntaxException, IOException, ParseException {
        return covidService.getCountryIntervalHistory(country.toLowerCase(), initial, end);
    }

    @GetMapping("/cache")
    public List<Request> getCache() {
        return cacheService.getCache();
    }

}
