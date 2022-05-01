package com.hw1.app.covid_service.cache;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.junit.jupiter.api.*;

class CacheTest {

    private Cache cache;    //60 sec default TTL
    private Cache cacheTTL;
    private Request request;
    private Long id;
    private Date created_at;
    private String country;
    private Integer fetchDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Statistic> statistics;

    @DisplayName("Set up cache")
    @BeforeEach
    void setUp() throws Exception {
        this.cache = new Cache();
        this.request = new Request();
        this.id = 1L;
        this.request.setId(id);
        this.created_at = new Date();
        this.request.setCreatedAt(created_at);
        this.country = "Portugal";
        this.request.setCountry(country);
        this.fetchDays = 7;
        this.request.setFetchDays(fetchDays);
        this.endDate = this.created_at.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.request.setEndDate(endDate);
        this.statistics = new ArrayList<>();
        this.request.setStatistics(statistics);
        this.startDate = endDate.minusDays(this.fetchDays);
        this.cacheTTL = new Cache(500);
    }

    @DisplayName("Add request to cache")
    @Test
    void testStoreRequestStatistics() {
        assertEquals(0, this.cache.getCache().size());
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);
        assertEquals(1, this.cache.getCache().size());
    }

    @DisplayName("Find by key")
    @Test
    void testFindByKey() {
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);

        Request actualRequest = this.cache.findByKey(key);
        assertEquals(this.request, actualRequest);
    }   
    
    @DisplayName("Get request statistics in cache")
    @Test
    void testGetRequestStatisticsHIT() {
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);

        Date start = java.util.Date.from(this.startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = java.util.Date.from(this.endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Request actualRequest = this.cache.getRequestStatistics(this.country, start, end, this.fetchDays);
        assertEquals(this.request, actualRequest);
        assertEquals(this.id, actualRequest.getId());
        assertEquals(this.created_at, actualRequest.getCreatedAt());
        assertEquals(this.country, actualRequest.getCountry());
        assertEquals(this.fetchDays, actualRequest.getFetchDays());
        assertEquals(this.endDate, actualRequest.getEndDate());
        assertEquals(CacheStatus.HIT, actualRequest.getCacheStatus());
        assertEquals(this.statistics, actualRequest.getStatistics());
    }

    @DisplayName("Get request statistics in cache for included fetch days")
    @Test
    void testGetRequestStatisticsHITForLessDays() {

        this.request.setFetchDays(365);
        String key = this.cache.generateKey(this.country, this.endDate, this.request.getFetchDays());
        this.cache.storeRequestStatistics(key, this.request);

        Date start = java.util.Date.from(this.startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = java.util.Date.from(this.endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Request actualRequest = this.cache.getRequestStatistics(this.country, start, end, 31);
        assertEquals(this.request, actualRequest);
        assertEquals(this.id, actualRequest.getId());
        assertEquals(this.created_at, actualRequest.getCreatedAt());
        assertEquals(this.country, actualRequest.getCountry());
        assertEquals(this.request.getFetchDays(), actualRequest.getFetchDays());
        assertEquals(this.endDate, actualRequest.getEndDate());
        assertEquals(CacheStatus.HIT, actualRequest.getCacheStatus());
        assertEquals(this.statistics, actualRequest.getStatistics());
    }

    @DisplayName("Get request statistics not in cache")
    @Test
    void testGetRequestStatisticsMISS() {
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);

        Date start = java.util.Date.from(this.startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = java.util.Date.from(this.endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Request actualRequest = this.cache.getRequestStatistics("France", start, end, this.fetchDays);
        assertNull(actualRequest);

    }

    @DisplayName("Get request statistics expired in cache")
    @Test
    void testGetRequestStatisticsExpired() {
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        Date expiredDate = new Date(this.request.getCreatedAt().getTime() - 100000 * 1000);
        this.request.setCreatedAt(expiredDate);
        this.cacheTTL.storeRequestStatistics(key, this.request);

        Date start = java.util.Date.from(this.startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = java.util.Date.from(this.endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Request actualRequest = this.cacheTTL.getRequestStatistics(this.country, start, end, this.fetchDays);
        assertNull(actualRequest);
    }

    @DisplayName("Test the key generation")
    @Test
    void testGenerateKey() {
        String expectedKey = this.country + this.endDate.toString() + this.fetchDays;
        String actualKey = this.cache.generateKey(country, endDate, fetchDays);
        assertEquals(expectedKey, actualKey);
    }

    @DisplayName("Deleting from cache")
    @Test
    void testDeleteRequestStatistics() {

        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);

        this.cache.deleteRequestStatistics(key);
        assertEquals(0,this.cache.getCache().size());
        assertNull(this.cache.findByKey(key));
    }

    @DisplayName("Get cache")
    @Test
    void testGetCache() {

        List<Request> expectedCache = new ArrayList<>();
        expectedCache.add(this.request);
        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        this.cache.storeRequestStatistics(key, this.request);

        assertEquals(1,this.cache.getCache().size());
        assertEquals(expectedCache, this.cache.getCache());
    }

    @DisplayName("Get cache and delete expired")
    @Test
    void testGetCacheDeleteExpired() {


        String key = this.cache.generateKey(this.country, this.endDate, this.fetchDays);
        Date expiredDate = new Date(this.request.getCreatedAt().getTime() - 1000000 * 1000);
        this.request.setCreatedAt(expiredDate);
        this.cacheTTL.storeRequestStatistics(key, this.request);

        List<Request> cacheTTL = this.cacheTTL.getCache();

        assertEquals(0,cacheTTL.size());
        assertNull(this.cacheTTL.findByKey(key));

    }

    @DisplayName("Test if the date has expired (valid)")
    @Test
    void testIsExpiredValid() {

        this.request.setCreatedAt(new Date(this.request.getCreatedAt().getTime() - 100000 * 1000));
        assertTrue(this.cache.isExpired(this.request));
    }

    @DisplayName("Test if the date has expired (Invalid)")
    @Test
    void testIsExpiredInvalid() {
        
        assertFalse(this.cache.isExpired(this.request));
    }



    @DisplayName("Clear the cache")
    @AfterEach
    void clearCache() {
        this.cache.clearCache();
    }
    
}
