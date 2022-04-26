package com.hw1.app.covid_service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;
import com.hw1.app.covid_service.service.CacheService;
import com.hw1.app.covid_service.service.CovidService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

        when(covidService.getCountryIntervalHistory(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(request);

        mvc.perform(get("/interval_history")
            .param("country", country)
            .param("initial", "22-04-2021")
            .param("end", "22-04-2022")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtils.toJson(request)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.id", is(request.getId().intValue())))
            .andExpect(jsonPath("$.country", is(request.getCountry())))
            .andExpect(jsonPath("$.endDate", is(request.getEndDate().toString())))
            .andExpect(jsonPath("$.cacheStatus", is(request.getCacheStatus().toString())))
            .andExpect(jsonPath("$.statistics", hasSize(equalTo(1))))
            .andExpect(jsonPath("$.statistics[0].id", is(id.intValue())))
            .andExpect(jsonPath("$.statistics[0].country", is(country)))
            .andExpect(jsonPath("$.statistics[0].continent", is(continent)))
            .andExpect(jsonPath("$.statistics[0].population", is(population)))
            .andExpect(jsonPath("$.statistics[0].newCases", is(newCases)))
            .andExpect(jsonPath("$.statistics[0].recovered", is(recovered)))
            .andExpect(jsonPath("$.statistics[0].totalCases", is(totalCases)))
            .andExpect(jsonPath("$.statistics[0].newCritical", is(newCritical)))
            .andExpect(jsonPath("$.statistics[0].active", is(active)))
            .andExpect(jsonPath("$.statistics[0].casesPerMillion", is(casesPerMillion)))
            .andExpect(jsonPath("$.statistics[0].totalTests", is(totalTests)))
            .andExpect(jsonPath("$.statistics[0].testsPerMillion", is(testsPerMillion)))
            .andExpect(jsonPath("$.statistics[0].newDeaths", is(newDeaths)))
            .andExpect(jsonPath("$.statistics[0].totalDeaths", is(totalDeaths)))
            .andExpect(jsonPath("$.statistics[0].deathsPerMillion", is(deathsPerMillion)))
            .andExpect(jsonPath("$.statistics[0].time", is(time.toString())))
            .andExpect(jsonPath("$.statistics[0].request").doesNotExist())
            .andExpect(jsonPath("$.createdAt", is(request.getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toString().replace("Z",":00.000+00:00"))));

        verify(covidService, times(1)).getCountryIntervalHistory(Mockito.anyString(),Mockito.any(),Mockito.any());
    }

    @Test
    void getCache() throws Exception {

        List<Request> cache = new ArrayList<>();
        Date created_at = new Date();
        cache.add(new Request(1L, created_at, "Portugal", 365, LocalDate.of(2022, 4, 22), CacheStatus.HIT, new ArrayList<Statistic>()));
        cache.add(new Request(2L, created_at, "Spain", 7, LocalDate.of(2022, 4, 22), CacheStatus.MISS, new ArrayList<Statistic>()));


        when(cacheService.getCache()).thenReturn(cache);

        mvc.perform(get("/cache").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(cache)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(equalTo(2))))
            .andExpect(jsonPath("$[0].id", is(cache.get(0).getId().intValue())))
            .andExpect(jsonPath("$[0].country", is(cache.get(0).getCountry())))
            .andExpect(jsonPath("$[0].endDate", is(cache.get(0).getEndDate().toString())))
            .andExpect(jsonPath("$[0].cacheStatus", is(cache.get(0).getCacheStatus().toString())))
            .andExpect(jsonPath("$[0].statistics", is(cache.get(0).getStatistics())))
            .andExpect(jsonPath("$[0].createdAt", is(cache.get(0).getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toString().replace("Z","+00:00"))))
            .andExpect(jsonPath("$[1].id", is(cache.get(1).getId().intValue())))
            .andExpect(jsonPath("$[1].country", is(cache.get(1).getCountry())))
            .andExpect(jsonPath("$[1].endDate", is(cache.get(1).getEndDate().toString())))
            .andExpect(jsonPath("$[1].cacheStatus", is(cache.get(1).getCacheStatus().toString())))
            .andExpect(jsonPath("$[1].statistics", is(cache.get(1).getStatistics())))
            .andExpect(jsonPath("$[1].createdAt", is(cache.get(1).getCreatedAt().toInstant().atOffset(ZoneOffset.UTC).toString().replace("Z","+00:00"))));

        verify(cacheService, times(1)).getCache();
    }
    
}
