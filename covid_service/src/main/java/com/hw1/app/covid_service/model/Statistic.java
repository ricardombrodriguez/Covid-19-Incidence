package com.hw1.app.covid_service.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    private Integer new_cases;

    @Column
    private Integer recovered;

    @Column
    private Integer total_cases;

    @Column
    private Integer new_critical;

    @Column
    private Integer active;

    @Column
    private Double cases_per_million;

    @Column
    private Integer total_tests;

    @Column
    private Double tests_per_million;

    @Column
    private Integer new_deaths;

    @Column
    private Integer total_deaths;

    @Column
    private Double deaths_per_million;

    @Column
    private LocalDate time;

    public Statistic(String country, String continent, Integer population, Integer new_cases, Integer recovered,
            Integer total_cases, Integer new_critical, Integer active, Double cases_per_million, Integer total_tests,
            Double tests_per_million, Integer new_deaths, Integer total_deaths, Double deaths_per_million,
            LocalDate time) {
        this.country = country;
        this.continent = continent;
        this.population = population;
        this.new_cases = new_cases;
        this.recovered = recovered;
        this.total_cases = total_cases;
        this.new_critical = new_critical;
        this.active = active;
        this.cases_per_million = cases_per_million;
        this.total_tests = total_tests;
        this.tests_per_million = tests_per_million;
        this.new_deaths = new_deaths;
        this.total_deaths = total_deaths;
        this.deaths_per_million = deaths_per_million;
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

    public Integer getNew_cases() {
        return new_cases;
    }

    public void setNew_cases(Integer new_cases) {
        this.new_cases = new_cases;
    }

    public Integer getRecovered() {
        return recovered;
    }

    public void setRecovered(Integer recovered) {
        this.recovered = recovered;
    }

    public Integer getTotal_cases() {
        return total_cases;
    }

    public void setTotal_cases(Integer total_cases) {
        this.total_cases = total_cases;
    }

    public Integer getNew_critical() {
        return new_critical;
    }

    public void setNew_critical(Integer new_critical) {
        this.new_critical = new_critical;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Double getCases_per_million() {
        return cases_per_million;
    }

    public void setCases_per_million(Double cases_per_million) {
        this.cases_per_million = cases_per_million;
    }

    public Integer getTotal_tests() {
        return total_tests;
    }

    public void setTotal_tests(Integer total_tests) {
        this.total_tests = total_tests;
    }

    public Double getTests_per_million() {
        return tests_per_million;
    }

    public void setTests_per_million(Double tests_per_million) {
        this.tests_per_million = tests_per_million;
    }

    public Integer getNew_deaths() {
        return new_deaths;
    }

    public void setNew_deaths(Integer new_deaths) {
        this.new_deaths = new_deaths;
    }

    public Integer getTotal_deaths() {
        return total_deaths;
    }

    public void setTotal_deaths(Integer total_deaths) {
        this.total_deaths = total_deaths;
    }

    public Double getDeaths_per_million() {
        return deaths_per_million;
    }

    public void setDeaths_per_million(Double deaths_per_million) {
        this.deaths_per_million = deaths_per_million;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((cases_per_million == null) ? 0 : cases_per_million.hashCode());
        result = prime * result + ((continent == null) ? 0 : continent.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((deaths_per_million == null) ? 0 : deaths_per_million.hashCode());
        result = prime * result + ((new_cases == null) ? 0 : new_cases.hashCode());
        result = prime * result + ((new_critical == null) ? 0 : new_critical.hashCode());
        result = prime * result + ((new_deaths == null) ? 0 : new_deaths.hashCode());
        result = prime * result + ((population == null) ? 0 : population.hashCode());
        result = prime * result + ((recovered == null) ? 0 : recovered.hashCode());
        result = prime * result + ((tests_per_million == null) ? 0 : tests_per_million.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((total_cases == null) ? 0 : total_cases.hashCode());
        result = prime * result + ((total_deaths == null) ? 0 : total_deaths.hashCode());
        result = prime * result + ((total_tests == null) ? 0 : total_tests.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Statistic other = (Statistic) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (cases_per_million == null) {
            if (other.cases_per_million != null)
                return false;
        } else if (!cases_per_million.equals(other.cases_per_million))
            return false;
        if (continent == null) {
            if (other.continent != null)
                return false;
        } else if (!continent.equals(other.continent))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (deaths_per_million == null) {
            if (other.deaths_per_million != null)
                return false;
        } else if (!deaths_per_million.equals(other.deaths_per_million))
            return false;
        if (new_cases == null) {
            if (other.new_cases != null)
                return false;
        } else if (!new_cases.equals(other.new_cases))
            return false;
        if (new_critical == null) {
            if (other.new_critical != null)
                return false;
        } else if (!new_critical.equals(other.new_critical))
            return false;
        if (new_deaths == null) {
            if (other.new_deaths != null)
                return false;
        } else if (!new_deaths.equals(other.new_deaths))
            return false;
        if (population == null) {
            if (other.population != null)
                return false;
        } else if (!population.equals(other.population))
            return false;
        if (recovered == null) {
            if (other.recovered != null)
                return false;
        } else if (!recovered.equals(other.recovered))
            return false;
        if (tests_per_million == null) {
            if (other.tests_per_million != null)
                return false;
        } else if (!tests_per_million.equals(other.tests_per_million))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (total_cases == null) {
            if (other.total_cases != null)
                return false;
        } else if (!total_cases.equals(other.total_cases))
            return false;
        if (total_deaths == null) {
            if (other.total_deaths != null)
                return false;
        } else if (!total_deaths.equals(other.total_deaths))
            return false;
        if (total_tests == null) {
            if (other.total_tests != null)
                return false;
        } else if (!total_tests.equals(other.total_tests))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Statistic [active=" + active + ", cases_per_million=" + cases_per_million + ", continent=" + continent
                + ", country=" + country + ", deaths_per_million=" + deaths_per_million + ", new_cases="
                + new_cases + ", new_critical=" + new_critical + ", new_deaths=" + new_deaths + ", population="
                + population + ", recovered=" + recovered + ", tests_per_million=" + tests_per_million + ", time="
                + time + ", total_cases=" + total_cases + ", total_deaths=" + total_deaths + ", total_tests="
                + total_tests + "]";
    }

}
