package com.hw1.app.covid_service.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.hw1.app.covid_service.model.Request;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByCountryAndStartDateAndFetchDays(String country, LocalDate startDate, Integer fetchDays);
    
}
