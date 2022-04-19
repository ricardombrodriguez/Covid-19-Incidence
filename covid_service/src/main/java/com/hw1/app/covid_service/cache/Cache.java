package com.hw1.app.covid_service.cache;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.repository.RequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cache {

    @Autowired
    private RequestRepository requestRepository;

    private static Logger logger = LogManager.getLogger(Cache.class);

    private Integer TTL;        // Time-To-Live (minutes)

    private List<Integer> allFetchDays = new ArrayList<>();

    public Cache() {
        this.TTL = 60;
        this.allFetchDays = Arrays.asList(0,7,31,365);
    }

    public Cache(Integer TTL) {
        this.TTL = TTL;
        this.allFetchDays = Arrays.asList(0,7,31,365);
    }

    public Request getRequestStatistics(String country, LocalDate startDate, Integer fetchDays) {

        logger.info("[CACHE] Retrieving request statistics for country {}, starting date {} and for {} fetch days", country, startDate, fetchDays);
        Integer currentIndex = this.allFetchDays.indexOf(fetchDays);
        Request req = null;

        // Example: If a user searches for last week statistics in 'Portugal' and he previously searched for last month stats, the results he
        // gets from the last week are fetched from the cache, since last month stats include last week ones...
        for (int i = currentIndex; i < allFetchDays.size(); i++) {
            logger.info("[CACHE] Verifying if the data exists for {} fetch days", fetchDays);
            System.out.println("CHECKING FOR " + fetchDays);
            req = requestRepository.findByCountryAndStartDateAndFetchDays(country, startDate, this.allFetchDays.get(currentIndex)).orElse(null);
            if (req != null) break;
        }

        if (req != null) {

            //Check if the request expired
            if (isExpired(req)) {

                logger.info("[CACHE] Expired request statistics for country {}, starting date {} and for {} fetch days", country, startDate, fetchDays);
                deleteRequestStatistics(req);
                System.out.println("EXPIRED");

            } else {

                System.out.println("success");

                logger.info("[CACHE] Successful retrieving of request statistics for country {}, starting date {} and for {} fetch days", country, startDate, fetchDays);
                req.setCreated_at(new Date(System.currentTimeMillis()));    // Update cache 'created_at'

            }

        } else {
            
            logger.info("[CACHE] Could not find request statistics for country {}, starting date {} and for {} fetch days", country, startDate, fetchDays);
            System.out.println("could not find");


        }

        return req;
        
    }

    private void deleteRequestStatistics(Request req) {

        logger.info("[CACHE] Deleting expired request statistics for country {}, starting date {} and for {} fetch days", req.getCountry(), req.getStartDate(), req.getFetchDays());
        requestRepository.delete(req);

    }

    // Returns true if the Request persists more than the limitDate (1000ms * time-to-live) in the cache db
    private boolean isExpired(Request req) {
        
        Date limitDate = new Date(System.currentTimeMillis() - 1000 * this.TTL);
        return limitDate.after(req.getCreated_at());

    }
    
}
