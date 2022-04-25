package com.hw1.app.covid_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;
import com.hw1.app.covid_service.resolver.RapidApiResolver;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CovidServiceTest {

    @Mock
    private Cache cache;

    @Mock
    private RapidApiResolver rapidApiResolver;

    @InjectMocks
    private CovidService covidService;

    @Test
    void testGetAllCountries() throws URISyntaxException, IOException, ParseException {


        List<String> countries = new ArrayList<>();
        countries.add("Portugal");
        countries.add("France");
        countries.add("UK");

        Mockito.when(covidService.getAllCountries()).thenReturn(countries);

        List<String> actualCountries = covidService.getAllCountries();

        Mockito.verify(rapidApiResolver, times(1)).getCountries();
        assertThat(actualCountries).isEqualTo(countries);

    }

    @Test
    void testGetCountryIntervalHistory() throws URISyntaxException, IOException, ParseException, java.text.ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");  
        Date initial = format.parse("22-04-2021");
        Date end = format.parse("22-04-2022");

        List<Statistic> statistics = new ArrayList<>();
        Long id = 1L;
        String country = "Portugal";
        String continent = "Europe";
        Integer population = 10000000;
        Integer newCases = 5000;
        Integer recovered = 4000;
        Integer totalCases = 3242580;
        Integer newCritical = 10;
        Integer active = 30000;
        Double casesPerMillion = 323258.0;
        Integer totalTests = 20000000;
        Double testsPerMillion = 200000.0;
        Integer newDeaths = 40;
        Integer totalDeaths = 40000;
        Double deathsPerMillion = 310.2;
        LocalDate time = LocalDate.of(2022, 04, 22);
        Statistic statistic = new Statistic(id, country, continent, population, newCases, recovered, totalCases, newCritical, active, casesPerMillion, totalTests, testsPerMillion, newDeaths, totalDeaths, deathsPerMillion, time);
        statistics.add(statistic);

        Request request = new Request(id, end, country, 365, time, CacheStatus.HIT, statistics);

        //Mockito.when(cache.storeRequestStatistics(anyString(), any())).doNothing();
        Mockito.when(covidService.getCountryIntervalHistory(country,initial,end)).thenReturn(request);

        Request actualRequest = covidService.getCountryIntervalHistory(country,initial,end);

        Mockito.verify(rapidApiResolver, times(1)).getCountryIntervalHistory(Mockito.anyString(), Mockito.any(), Mockito.any());
        Mockito.verify(rapidApiResolver, times(1)).getCountryHistory(Mockito.anyString(), Mockito.any());
        Mockito.verify(rapidApiResolver, times(1)).getJsonArray(Mockito.any());
        Mockito.verify(rapidApiResolver, atLeast(1)).parseStatistic(Mockito.any());
        assertThat(actualRequest).isEqualTo(request);

    }


    
}
