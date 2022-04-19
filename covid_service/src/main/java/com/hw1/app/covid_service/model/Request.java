package com.hw1.app.covid_service.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class Request {

    private Long id;
    private Date created_at;
    private String country;
    private Integer fetchDays;
    private LocalDate endDate;
    private CacheStatus cacheStatus;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "request")
    private List<Statistic> statistics;

    public Request() {}

    public Request(Date created_at, String country, Integer fetchDays) {
        this.created_at = created_at;
        this.country = country;
        this.fetchDays = fetchDays;
    }

    public Request(Date created_at, String country, Integer fetchDays, LocalDate endDate, CacheStatus cacheStatus) {
        this.created_at = created_at;
        this.country = country;
        this.fetchDays = fetchDays;
        this.endDate = endDate;   //end date / most recent
        this.cacheStatus = cacheStatus;
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

    public Integer getFetchDays() {
        return fetchDays;
    }

    public void setFetchDays(Integer fetchDays) {
        this.fetchDays = fetchDays;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public CacheStatus getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(CacheStatus cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    @Override
    public String toString() {
        return "Request [cacheStatus=" + cacheStatus + ", country=" + country + ", created_at=" + created_at
                + ", endDate=" + endDate + ", fetchDays=" + fetchDays + ", id=" + id + ", statistics=" + statistics
                + "]";
    }

    

}
