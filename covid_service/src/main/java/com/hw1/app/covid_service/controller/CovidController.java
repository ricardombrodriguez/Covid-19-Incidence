package com.hw1.app.covid_service.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.Statistic;
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
    private CovidService countryService;

    @GetMapping("/countries")
    public List<String> getAllCountries() throws URISyntaxException, IOException, ParseException {
        return countryService.getAllCountries();
    }

    @GetMapping("/interval_history")
    public List<Statistic> getCountryHistory(@RequestParam String country, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date initial, @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date end) throws URISyntaxException, IOException, ParseException {
        System.out.println(initial);
        System.out.println(end);
        return countryService.getCountryIntervalHistory(country, initial, end);
    }

}
