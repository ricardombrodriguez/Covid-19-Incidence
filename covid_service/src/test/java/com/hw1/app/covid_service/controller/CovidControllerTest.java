package com.hw1.app.covid_service.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;
import com.hw1.app.covid_service.service.CacheService;
import com.hw1.app.covid_service.service.CovidService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CovidController.class)
public class CovidControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CovidService covidService;

    @MockBean
    private CacheService cacheService;

    @Test
    void getAllCountries() throws Exception {

        List<String> countries = new ArrayList<>();
        countries.add("Portugal");
        countries.add("France");
        countries.add("Kiribati");

        when(covidService.getAllCountries()).thenReturn(countries);

        mvc.perform(get("/countries").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(countries)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(equalTo(3))))
            .andExpect(jsonPath("$[0]", is(countries.get(0))))
            .andExpect(jsonPath("$[1]", is(countries.get(1))))
            .andExpect(jsonPath("$[2]", is(countries.get(2))));

        verify(covidService, times(1)).getAllCountries();
    }

    @Test
    void getIntervalHistory() throws Exception {

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

        when(covidService.getCountryIntervalHistory("Portugal",initial,end)).thenReturn(request);

        mvc.perform(get("/interval_history")
            .param("country", country)
            .param("initial", "22-04-2021")
            .param("end", "22-04-2022")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(request)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$", hasSize(equalTo(1))))
            .andExpect(jsonPath("$[0].id", is(request.getId())))
            .andExpect(jsonPath("$.created_at", is(request.getCreated_at())))
            .andExpect(jsonPath("$.country", is(request.getCountry())));

        verify(covidService, times(1)).getCountryIntervalHistory(country, initial, end);
    }

    
}
