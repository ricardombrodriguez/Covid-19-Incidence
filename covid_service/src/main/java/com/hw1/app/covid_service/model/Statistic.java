package com.hw1.app.covid_service.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Statistic {

    @Id
    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String continent;

    @Column
    private Integer population;

    @Column
    private Integer newCases;

    @Column
    private Integer recovered;

    @Column
    private Integer totalCases;

    @Column
    private Integer newCritical;

    @Column
    private Integer active;

    @Column
    private Double casesPerMillion;

    @Column
    private Integer totalTests;

    @Column
    private Double testsPerMillion;

    @Column
    private Integer newDeaths;

    @Column
    private Integer totalDeaths;

    @Column
    private Double deathsPerMillion;

    @Column
    private LocalDate time;

    public Statistic(String country, String continent, Integer population, Integer newCases, Integer recovered,
            Integer totalCases, Integer newCritical, Integer active, Double casesPerMillion, Integer totalTests,
            Double testsPerMillion, Integer newDeaths, Integer totalDeaths, Double deathsPerMillion,
            LocalDate time) {
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

    public Integer getnewCritical() {
        return newCritical;
    }

    public void setnewCritical(Integer newCritical) {
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

}
