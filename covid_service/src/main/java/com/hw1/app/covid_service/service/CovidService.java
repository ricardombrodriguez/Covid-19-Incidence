package com.hw1.app.covid_service.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.resolver.RapidApiResolver;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CovidService {

    @Autowired
    private RapidApiResolver rapidApiResolver;

    @Autowired
    private Cache cache;

    public List<String> getAllCountries() throws URISyntaxException, IOException, ParseException {

        return rapidApiResolver.getCountries();
    }

    public Request getCountryIntervalHistory(String country, Date initial, Date end) throws URISyntaxException, IOException, ParseException {

        long timeDifference = end.getTime() - initial.getTime();
        int fetchDays = (int) (timeDifference / (1000 * 60 * 60* 24));

        Request req = cache.getRequestStatistics(country, initial, fetchDays);

        if (req == null) {

            try {

                req = rapidApiResolver.getCountryIntervalHistory(country, initial, end);
                cache.storeRequestStatistics(req);

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else {

            req.setCacheStatus(CacheStatus.MISS);

        }

        return req;

    }
    
}
