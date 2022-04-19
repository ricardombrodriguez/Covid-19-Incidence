package com.hw1.app.covid_service.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.Statistic;
import com.hw1.app.covid_service.resolver.RapidApiResolver;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CovidService {

    @Autowired
    private RapidApiResolver rapidApiResolver;

    public List<String> getAllCountries() throws URISyntaxException, IOException, ParseException {

        return rapidApiResolver.getCountries();
    }

    public List<Statistic> getCountryIntervalHistory(String country, Date initial, Date end) throws URISyntaxException, IOException, ParseException {

        return rapidApiResolver.getCountryIntervalHistory(country, initial, end);
    }
    
}
