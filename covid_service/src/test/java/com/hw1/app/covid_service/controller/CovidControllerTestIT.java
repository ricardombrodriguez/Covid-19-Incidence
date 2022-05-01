package com.hw1.app.covid_service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CovidControllerTestIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Cache cache;

    @AfterEach
    public void resetDb() {
        cache.clearCache();
    }

    @Test
    void whenValidRequest_thenCreateCacheMiss() throws Exception {

        assertThat(cache.getCache()).isEmpty();    

        mvc.perform(get("/interval_history?country=usa&initial=23-01-2021&end=23-01-2022").contentType(MediaType.APPLICATION_JSON));

        List<Request> found = cache.getCache();
        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(365);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.MISS);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache()).hasSize(1);      

    }

    @Test
    void whenDoubleRequest_thenCreateCacheHit() throws Exception {

        assertThat(cache.getCache()).isEmpty();    

        mvc.perform(get("/interval_history?country=usa&initial=23-01-2021&end=23-01-2022").contentType(MediaType.APPLICATION_JSON));

        List<Request> found = cache.getCache();
        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(365);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.MISS);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache()).hasSize(1);      

        // 31 days, should hit cache since 31 days are contained in the 365 day request 

        mvc.perform(get("/interval_history?country=usa&initial=23-12-2021&end=23-01-2022").contentType(MediaType.APPLICATION_JSON));

        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(31);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.HIT);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache()).hasSize(1);      

    }
    
    
}
