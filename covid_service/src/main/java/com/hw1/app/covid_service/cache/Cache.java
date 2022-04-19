package com.hw1.app.covid_service.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class Cache {

    @Autowired
    private RequestRepository requestRepository;

    private static Logger logger = LogManager.getLogger(Cache.class);

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

    public Request getRequestStatistics(String country, Date initial, Integer fetchDays) {

        logger.info("[CACHE] Retrieving request statistics for country {}, starting date {} and for {} fetch days", country, initial, fetchDays);
        Integer currentIndex = this.allFetchDays.indexOf(fetchDays);
        Request req = null;

        LocalDate initialDate = initial.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

        // Example: If a user searches for last week statistics in 'Portugal' and he previously searched for last month stats, the results he
        // gets from the last week are fetched from the cache, since last month stats include last week ones...
        for (int i = currentIndex; i < allFetchDays.size(); i++) {
            logger.info("[CACHE] Verifying if the data exists for {} fetch days", fetchDays);
            System.out.println("CHECKING FOR " + fetchDays);
            req = requestRepository.findByCountryAndStartDateAndFetchDays(country, initialDate, this.allFetchDays.get(currentIndex)).orElse(null);
            if (req != null) break;
        }

        if (req != null) {

            //Check if the request expired
            if (isExpired(req)) {

                logger.info("[CACHE] Expired request statistics for country {}, starting date {} and for {} fetch days", country, initial, fetchDays);
                deleteRequestStatistics(req);
                System.out.println("EXPIRED");

            } else {

                System.out.println("success");

                logger.info("[CACHE] Successful retrieving of request statistics for country {}, starting date {} and for {} fetch days", country, initial, fetchDays);
                req.setCreated_at(new Date(System.currentTimeMillis()));    // Update cache 'created_at'
                req.setCacheStatus(CacheStatus.HIT);

            }

        } else {
            
            logger.info("[CACHE] Could not find request statistics for country {}, starting date {} and for {} fetch days", country, initial, fetchDays);

        }

        return req;
        
    }

    public void deleteRequestStatistics(Request req) {

        logger.info("[CACHE] Deleting expired request statistics for country {}, starting date {} and for {} fetch days", req.getCountry(), req.getStartDate(), req.getFetchDays());
        requestRepository.delete(req);

    }

    // Returns true if the Request persists more than the limitDate (1000ms * time-to-live) in the cache db
    public boolean isExpired(Request req) {
        
        Date limitDate = new Date(System.currentTimeMillis() - 1000 * this.TTL);
        return limitDate.after(req.getCreated_at());

    }

    public void storeRequestStatistics(Request req) {

        logger.info("[CACHE] Storing request statistics for country {}, starting date {} and for {} fetch days", req.getCountry(), req.getStartDate(), req.getFetchDays());
        requestRepository.saveAndFlush(req);

    }
    
}
