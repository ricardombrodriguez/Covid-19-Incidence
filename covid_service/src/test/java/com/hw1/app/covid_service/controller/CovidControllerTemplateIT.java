package com.hw1.app.covid_service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import com.hw1.app.covid_service.cache.Cache;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class CovidControllerTemplateIT {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Cache cache;

    @AfterEach
    public void resetDb() {
        cache.clearCache();
    }


    @Test
    void whenValidRequest_thenCreateCacheMiss() {

        ResponseEntity<Request> response = restTemplate
                .exchange("/interval_history?country=usa&initial=23-01-2021&end=23-01-2022", HttpMethod.GET, null, new ParameterizedTypeReference<Request>() {
                });

        List<Request> found = cache.getCache();
        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(365);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.MISS);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache().size()).isEqualTo(1);   
    }

    @Test
    void whenDoubleRequest_thenCreateCacheHitAndOthers()  {

        assertThat(cache.getCache().size()).isEqualTo(0);   

        ResponseEntity<Request> response = restTemplate
                .exchange("/interval_history?country=usa&initial=23-01-2021&end=23-01-2022", HttpMethod.GET, null, new ParameterizedTypeReference<Request>() {
                });

        List<Request> found = cache.getCache();
        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(365);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.MISS);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache().size()).isEqualTo(1);   

        // 31 days, should hit cache since 31 days are contained in the 365 day request 

        response = restTemplate
        .exchange("/interval_history?country=usa&initial=23-12-2021&end=23-01-2022", HttpMethod.GET, null, new ParameterizedTypeReference<Request>() {
        });

        assertThat(found).extracting(Request::getCountry).containsOnly("usa");
        assertThat(found).extracting(Request::getFetchDays).containsOnly(31);
        assertThat(found).extracting(Request::getCacheStatus).containsOnly(CacheStatus.HIT);
        assertThat(found).extracting(Request::getEndDate).containsOnly(LocalDate.of(2021, 01, 23));
        assertThat(found).extracting(Request::getCreatedAt).isNotNull();
        assertThat(found).extracting(Request::getStatistics).isNotNull();

        assertThat(cache.getCache().size()).isEqualTo(1);  

    }



}
