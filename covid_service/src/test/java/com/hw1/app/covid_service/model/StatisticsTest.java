package com.hw1.app.covid_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class StatisticsTest {

    @Test
    void getStatisticObject() {

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
        Request request = new Request();

        Statistic statistic = new Statistic(id, country, continent, population, newCases, recovered, totalCases, newCritical, active, casesPerMillion, totalTests, testsPerMillion, newDeaths, totalDeaths, deathsPerMillion, time);
        statistic.setRequest(request);
    
        assertEquals(id, statistic.getId());
        assertEquals(country, statistic.getCountry());
        assertEquals(continent, statistic.getContinent());
        assertEquals(population, statistic.getPopulation());
        assertEquals(newCases, statistic.getNewCases());
        assertEquals(recovered, statistic.getRecovered());
        assertEquals(totalCases, statistic.getTotalCases());
        assertEquals(newCritical, statistic.getNewCritical());
        assertEquals(active, statistic.getActive());
        assertEquals(casesPerMillion, statistic.getCasesPerMillion());
        assertEquals(totalTests, statistic.getTotalTests());
        assertEquals(testsPerMillion, statistic.getTestsPerMillion());
        assertEquals(newDeaths, statistic.getNewDeaths());
        assertEquals(totalDeaths, statistic.getTotalDeaths());
        assertEquals(deathsPerMillion, statistic.getDeathsPerMillion());
        assertEquals(time, statistic.getTime());
        assertEquals(request, statistic.getRequest());

    }

    @Test
    void getStatisticObjectOtherConstructor() {

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
        Request request = new Request();

        Statistic statistic = new Statistic(country, continent, population, newCases, recovered, totalCases, newCritical, active, casesPerMillion, totalTests, testsPerMillion, newDeaths, totalDeaths, deathsPerMillion, time);
        statistic.setRequest(request);
    
        assertEquals(country, statistic.getCountry());
        assertEquals(continent, statistic.getContinent());
        assertEquals(population, statistic.getPopulation());
        assertEquals(newCases, statistic.getNewCases());
        assertEquals(recovered, statistic.getRecovered());
        assertEquals(totalCases, statistic.getTotalCases());
        assertEquals(newCritical, statistic.getNewCritical());
        assertEquals(active, statistic.getActive());
        assertEquals(casesPerMillion, statistic.getCasesPerMillion());
        assertEquals(totalTests, statistic.getTotalTests());
        assertEquals(testsPerMillion, statistic.getTestsPerMillion());
        assertEquals(newDeaths, statistic.getNewDeaths());
        assertEquals(totalDeaths, statistic.getTotalDeaths());
        assertEquals(deathsPerMillion, statistic.getDeathsPerMillion());
        assertEquals(time, statistic.getTime());
        assertEquals(request, statistic.getRequest());

    }
    
}
