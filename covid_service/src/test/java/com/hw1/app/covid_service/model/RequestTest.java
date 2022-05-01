package com.hw1.app.covid_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;


public class RequestTest {

    @Test
    void getRequestObject() {

        Date created_at = new Date();
        LocalDate endDate = created_at.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    
        ArrayList<Statistic> statistics = new ArrayList<Statistic>() { {add(new Statistic());} };
        Request request = new Request(1L, created_at, "Portugal", 365, endDate, CacheStatus.HIT, statistics);
    
        assertEquals(1L, request.getId());
        assertEquals("Portugal", request.getCountry());
        assertEquals(created_at, request.getCreatedAt());
        assertEquals(365, request.getFetchDays());
        assertEquals(endDate, request.getEndDate());
        assertEquals(CacheStatus.HIT, request.getCacheStatus());
        assertEquals(statistics, request.getStatistics());

    }

    @Test
    void getRequestObjectOtherConstructor() {

        Date created_at = new Date();
        LocalDate endDate = created_at.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Request request = new Request(created_at, "Portugal", 365, endDate, CacheStatus.HIT);
    
        assertEquals("Portugal", request.getCountry());
        assertEquals(created_at, request.getCreatedAt());
        assertEquals(365, request.getFetchDays());
        assertEquals(endDate, request.getEndDate());
        assertEquals(CacheStatus.HIT, request.getCacheStatus());

    }

}
