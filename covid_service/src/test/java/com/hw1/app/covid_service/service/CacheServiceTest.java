package com.hw1.app.covid_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @Mock
    private Cache cache;

    @InjectMocks
    private CacheService cacheService;

    private Date created_at;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException, ParseException {

        List<Request> cache = new ArrayList<>();
        this.created_at = new Date();
        cache.add(new Request(1L, created_at, "Portugal", 365, LocalDate.of(2022, 4, 22), CacheStatus.HIT, new ArrayList<Statistic>()));
        cache.add(new Request(2L, created_at, "Spain", 7, LocalDate.of(2022, 4, 22), CacheStatus.MISS, new ArrayList<Statistic>()));

        // Expectations
        Mockito.when(cacheService.getCache()).thenReturn(cache);

    }

    @Test
    void testCache() throws URISyntaxException, IOException, ParseException {

        List<Request> cache = cacheService.getCache();
        assertEquals(2, cache.size());
        assertEquals(1L, cache.get(0).getId());
        assertEquals(this.created_at, cache.get(0).getCreatedAt());
        assertEquals("Portugal", cache.get(0).getCountry());
        assertEquals(LocalDate.of(2022, 4, 22), cache.get(0).getEndDate());
        assertEquals(CacheStatus.HIT, cache.get(0).getCacheStatus());
        assertEquals(365, cache.get(0).getFetchDays());
        assertEquals(new ArrayList<Statistic>(), cache.get(0).getStatistics());
        assertEquals(2L, cache.get(1).getId());
        assertEquals(this.created_at, cache.get(1).getCreatedAt());
        assertEquals("Spain", cache.get(1).getCountry());
        assertEquals(LocalDate.of(2022, 4, 22), cache.get(1).getEndDate());
        assertEquals(CacheStatus.MISS, cache.get(1).getCacheStatus());
        assertEquals(7, cache.get(1).getFetchDays());
        assertEquals(new ArrayList<Statistic>(), cache.get(1).getStatistics());

    }

    @AfterEach
    void clearCache() {
        this.cache.clearCache();
    }

    
}
