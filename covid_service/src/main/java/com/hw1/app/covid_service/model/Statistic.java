package com.hw1.app.covid_service.model;

import java.time.LocalDate;

public class Statistic {

    private Long id;
    private String country;
    private String continent;
    private Integer population;
    private Integer newCases;
    private Integer recovered;
    private Integer totalCases;
    private Integer newCritical;
    private Integer active;
    private Double casesPerMillion;
    private Integer totalTests;
    private Double testsPerMillion;
    private Integer newDeaths;
    private Integer totalDeaths;
    private Double deathsPerMillion;
    private LocalDate time;
    private Request request;

    public Statistic(Long id, String country, String continent, Integer population, Integer newCases, Integer recovered,
            Integer totalCases, Integer newCritical, Integer active, Double casesPerMillion, Integer totalTests,
            Double testsPerMillion, Integer newDeaths, Integer totalDeaths, Double deathsPerMillion,
            LocalDate time) {
        this.id = id;
        this.country = country;
        this.continent = continent;
        this.population = population;
        this.newCases = newCases;
        this.recovered = recovered;
        this.totalCases = totalCases;
        this.newCritical = newCritical;
        this.active = active;
        this.casesPerMillion = casesPerMillion;
        this.totalTests = totalTests;
        this.testsPerMillion = testsPerMillion;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
        this.deathsPerMillion = deathsPerMillion;
        this.time = time;
    }

    

    public Statistic(String country, String continent, Integer population, Integer newCases, Integer recovered,
            Integer totalCases, Integer newCritical, Integer active, Double casesPerMillion, Integer totalTests,
            Double testsPerMillion, Integer newDeaths, Integer totalDeaths, Double deathsPerMillion, LocalDate time) {
        this.country = country;
        this.continent = continent;
        this.population = population;
        this.newCases = newCases;
        this.recovered = recovered;
        this.totalCases = totalCases;
        this.newCritical = newCritical;
        this.active = active;
        this.casesPerMillion = casesPerMillion;
        this.totalTests = totalTests;
        this.testsPerMillion = testsPerMillion;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
        this.deathsPerMillion = deathsPerMillion;
        this.time = time;
    }

    public Statistic() {
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getCountry() {
        return country;
    }



    public void setCountry(String country) {
        this.country = country;
    }



    public String getContinent() {
        return continent;
    }



    public void setContinent(String continent) {
        this.continent = continent;
    }



    public Integer getPopulation() {
        return population;
    }



    public void setPopulation(Integer population) {
        this.population = population;
    }



    public Integer getNewCases() {
        return newCases;
    }



    public void setNewCases(Integer newCases) {
        this.newCases = newCases;
    }



    public Integer getRecovered() {
        return recovered;
    }



    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }



    public Integer getTotalCases() {
        return totalCases;
    }



    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }



    public Integer getNewCritical() {
        return newCritical;
    }



    public void setNewCritical(Integer newCritical) {
        this.newCritical = newCritical;
    }



    public Integer getActive() {
        return active;
    }



    public void setActive(Integer active) {
        this.active = active;
    }



    public Double getCasesPerMillion() {
        return casesPerMillion;
    }



    public void setCasesPerMillion(Double casesPerMillion) {
        this.casesPerMillion = casesPerMillion;
    }



    public Integer getTotalTests() {
        return totalTests;
    }



    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
    }



    public Double getTestsPerMillion() {
        return testsPerMillion;
    }



    public void setTestsPerMillion(Double testsPerMillion) {
        this.testsPerMillion = testsPerMillion;
    }



    public Integer getNewDeaths() {
        return newDeaths;
    }



    public void setNewDeaths(Integer newDeaths) {
        this.newDeaths = newDeaths;
    }



    public Integer getTotalDeaths() {
        return totalDeaths;
    }



    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }



    public Double getDeathsPerMillion() {
        return deathsPerMillion;
    }



    public void setDeathsPerMillion(Double deathsPerMillion) {
        this.deathsPerMillion = deathsPerMillion;
    }



    public LocalDate getTime() {
        return time;
    }



    public void setTime(LocalDate time) {
        this.time = time;
    }



    public Request getRequest() {
        return request;
    }



    public void setRequest(Request request) {
        this.request = request;
    }



}
