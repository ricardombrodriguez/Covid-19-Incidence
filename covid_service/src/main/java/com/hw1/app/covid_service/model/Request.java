package com.hw1.app.covid_service.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column
    private Date created_at;

    @Column
    private String country;

    @Column
    private Integer fetchDays;

    @Column(nullable = true)
    private LocalDate startDate;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private CacheStatus cacheStatus;

    @Column(nullable = true)
    @OneToMany(mappedBy="request")
    private List<Statistic> statistics;

    public Request(Date created_at, String country, Integer fetchDays) {
        this.created_at = created_at;
        this.country = country;
        this.fetchDays = fetchDays;
    }

    public Request(Date created_at, String country, Integer fetchDays, LocalDate startDate,
            List<Statistic> statistics, CacheStatus cacheStatus) {
        this.created_at = created_at;
        this.country = country;
        this.fetchDays = fetchDays;
        this.statistics = statistics;
        this.startDate = statistics.get(0).getTime();   //initial date
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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

}
