package com.hw1.app.covid_service.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.hw1.app.covid_service.model.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE r.country = ?1 AND r.startDate = ?2 AND r.fetchDays = ?3")
    Optional<Request> findByCountryAndStartDateAndFetchDays(String country, LocalDate startDate, Integer fetchDays);
    
}
