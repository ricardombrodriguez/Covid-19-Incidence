package com.hw1.app.covid_service.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class Cache {

    private static Logger logger = LogManager.getLogger(Cache.class);

    private HashMap<String,Request> map = new HashMap<>();

    private Integer timeToLive;        // Time-To-Live (minutes)

    private List<Integer> allFetchDays = new ArrayList<>();

    public Cache() {
        this.timeToLive = 60;
        this.allFetchDays = Arrays.asList(0,7,30,365);
    }

    public Cache(Integer timeToLive) {
        this.timeToLive = timeToLive;
        this.allFetchDays = Arrays.asList(0,7,30,365);
    }

    public Request getRequestStatistics(String country, Date initial, Date end, Integer fetchDays) {

        logger.info("[CACHE] Retrieving request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
        Integer currentIndex = this.allFetchDays.indexOf(fetchDays);
        Request req = null;

        LocalDate initialDate = initial.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


        LocalDate endDate = end.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

        for (int i = currentIndex; i < this.allFetchDays.size(); i++) {

            logger.info("[CACHE] Verifying if the data exists for {} fetch days", this.allFetchDays.get(i));
            String currentKey = generateKey(country, endDate, fetchDays);
            req = findByKey(currentKey);

            if (req != null) {

                if (i != currentIndex) {
                    List<Statistic> requestStatistics = req.getStatistics();
                    requestStatistics.removeIf((Statistic stat) -> stat.getTime().isBefore(initialDate) || stat.getTime().isAfter(endDate));
                    req.setStatistics(requestStatistics);
                    req.setFetchDays(fetchDays);
                }
                break;
            }
        }

        if (req != null) {

            if (isExpired(req)) {

                logger.info("[CACHE] Expired request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
                String key = generateKey(country, endDate, fetchDays);
                deleteRequestStatistics(key);
                req = null;

            } else {
                
                logger.info("[CACHE] Successful retrieving of request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
                req.setCacheStatus(CacheStatus.HIT);

            }

        } else {
            
            logger.info("[CACHE] Could not find request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);

        }

        return req;
        
    }

    public Request findByKey(String key) {
        if (map.containsKey(key))
            return map.get(key);
        return null;
    }

    public void deleteRequestStatistics(String key) {

        Request req = map.get(key);
        logger.info("[CACHE] Deleting expired request statistics for country {}, ending date {} and for {} fetch days", req.getCountry(), req.getEndDate(), req.getFetchDays());
        map.remove(key);

    }

    public boolean isExpired(Request req) {
        
        Date limitDate = new Date(System.currentTimeMillis() - 1000 * this.timeToLive);
        return limitDate.after(req.getCreatedAt());

    }

    public void storeRequestStatistics(String key, Request req) {

        logger.info("[CACHE] Storing request statistics for country {}, ending date {} and for {} fetch days", req.getCountry(), req.getEndDate(), req.getFetchDays());
        map.put(key, req);

    }

    public List<Request> getCache() {

        List<Request> allCache = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, Request> entry : this.map.entrySet()) {
            if (isExpired(entry.getValue())) {
                toRemove.add(entry.getKey());
            } else {
                allCache.add(entry.getValue());
            }
        }

        for (String keyToRemove : toRemove) {
            deleteRequestStatistics(keyToRemove);
        }

        return allCache;
    }

    public void clearCache() {
        this.map.clear();
    }

    public String generateKey(String country, LocalDate endDate, Integer fetchDays) {
        return country + endDate.toString() + fetchDays;
    }
    
}
