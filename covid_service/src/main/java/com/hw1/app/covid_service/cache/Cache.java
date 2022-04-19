package com.hw1.app.covid_service.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class Cache {

    private static Logger logger = LogManager.getLogger(Cache.class);

    private HashMap<String,Request> cache = new HashMap<String,Request>();

    private Integer TTL;        // Time-To-Live (minutes)

    private List<Integer> allFetchDays = new ArrayList<>();

    public Cache() {
        this.TTL = 60;
        this.allFetchDays = Arrays.asList(0,7,30,365);
    }

    public Cache(Integer TTL) {
        this.TTL = TTL;
        this.allFetchDays = Arrays.asList(0,7,30,365);
    }

    public Request getRequestStatistics(String country, Date initial, Date end, Integer fetchDays, String key) {

        logger.info("[CACHE] Retrieving request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
        Integer currentIndex = this.allFetchDays.indexOf(fetchDays);
        Request req = null;

        LocalDate initialDate = initial.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();


        LocalDate endDate = end.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

        // Example: If a user searches for last week statistics in 'Portugal' and he previously searched for last month stats, the results he
        // gets from the last week are fetched from the cache, since last month stats include last week ones...
        for (int i = currentIndex; i < this.allFetchDays.size(); i++) {

            logger.info("[CACHE] Verifying if the data exists for {} fetch days", this.allFetchDays.get(i));
            String currentKey = country + endDate.toString() + this.allFetchDays.get(i);
            req = findByKey(currentKey);

            if (req != null) {
                // Retrieved cache from different fetch days that include the one I'm looking for (ex: last week includes last month)

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

            //Check if the request expired
            if (isExpired(req)) {

                logger.info("[CACHE] Expired request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
                deleteRequestStatistics(key);

            } else {
                
                logger.info("[CACHE] Successful retrieving of request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);
                req.setCacheStatus(CacheStatus.HIT);

            }

        } else {
            
            logger.info("[CACHE] Could not find request statistics for country {}, ending date {} and for {} fetch days", country, end, fetchDays);

        }

        return req;
        
    }

    private Request findByKey(String key) {
        if (cache.containsKey(key))
            return cache.get(key);
        return null;
    }

    public void deleteRequestStatistics(String key) {

        Request req = cache.get(key);
        logger.info("[CACHE] Deleting expired request statistics for country {}, ending date {} and for {} fetch days", req.getCountry(), req.getEndDate(), req.getFetchDays());
        cache.remove(key);

    }

    // Returns true if the Request persists more than the limitDate (1000ms * time-to-live) in the cache db
    public boolean isExpired(Request req) {
        
        Date limitDate = new Date(System.currentTimeMillis() - 1000 * this.TTL);
        return limitDate.after(req.getCreated_at());

    }

    public void storeRequestStatistics(String key, Request req) {

        logger.info("[CACHE] Storing request statistics for country {}, ending date {} and for {} fetch days", req.getCountry(), req.getEndDate(), req.getFetchDays());
        cache.put(key, req);

    }
    
}
