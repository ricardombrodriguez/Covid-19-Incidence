package com.hw1.app.covid_service.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hw1.app.covid_service.connection.HttpClient;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RapidApiResolverTest {
    
        @Mock
        HttpClient httpClient;
    
        @InjectMocks
        RapidApiResolver rapidApiResolver;
    
        @Test
        public void testGetAllCountries() throws IOException, URISyntaxException, ParseException {

            List<String> expected = new ArrayList<>();
            expected.add("Portugal");
            expected.add("Spain");
            expected.add("Chile");
    
            when(httpClient.doHttpGet("https://covid-193.p.rapidapi.com/countries"))
                .thenReturn("{\"response\":[\"Portugal\",\"Spain\",\"Chile\"]}");
    
            List<String> countries = rapidApiResolver.getCountries();
    
            assertThat(countries).isEqualTo(expected);
    
            verify(httpClient, times(1)).doHttpGet(anyString());
        }

        @Test
        public void testGetIntervalHistory() throws IOException, URISyntaxException, ParseException, java.text.ParseException {

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");  
            Date initial = format.parse("22-04-2021");
            Date end = format.parse("22-04-2022");
    
            List<Statistic> statistics = new ArrayList<>();
            Long id = 1L;
            String country = "Portugal";
            String continent = "Europe";
            Integer population = 10000000;
            Integer newCases = 5000;
            Integer recovered = 4000;
            Integer totalCases = 3242580;
            Integer newCritical = 10;
            Integer active = 30000;
            Double casesPerMillion = 323258.0;
            Integer totalTests = 20000000;
            Double testsPerMillion = 200000.0;
            Integer newDeaths = 40;
            Integer totalDeaths = 40000;
            Double deathsPerMillion = 310.2;
            LocalDate time = LocalDate.of(2022, 04, 22);
            Statistic statistic = new Statistic(id, country, continent, population, newCases, recovered, totalCases, newCritical, active, casesPerMillion, totalTests, testsPerMillion, newDeaths, totalDeaths, deathsPerMillion, time);
            statistics.add(statistic);
    
            Request expected = new Request(id, end, country, 365, time, CacheStatus.HIT, statistics);

            when(httpClient.doHttpGet("https://covid-193.p.rapidapi.com/history?country=Portugal"))
                .thenReturn("{\"response\":[{\"continent\":\"Europe\",\"country\":\"Portugal\",\"population\":10000000,\"cases\":{\"new\":\"5000\",\"active\":30000,\"critical\":10,\"recovered\":4000,\"1M_pop\":\"323258.0\",\"total\":3242580},\"deaths\":{\"new\":\"40\",\"1M_pop\":\"310.2\",\"total\":40000},\"tests\":{\"1M_pop\":\"200000.0\",\"total\":20000000},\"day\":\"2022-04-22\",\"time\":\"2022-04-22T23:30:03+00:00\"}]}");
    
            Request req = rapidApiResolver.getCountryIntervalHistory(country,initial,end);
    
            assertThat(req.getCountry()).isEqualTo(expected.getCountry());
            assertThat(req.getEndDate()).isEqualTo(initial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            assertThat(req.getFetchDays()).isEqualTo(expected.getFetchDays());
            assertThat(req.getStatistics()).hasSize(1);
            
            Statistic actual = req.getStatistics().get(0);
            assertThat(actual.getActive()).isEqualTo(statistic.getActive());
            assertThat(actual.getCasesPerMillion()).isEqualTo(statistic.getCasesPerMillion());
            assertThat(actual.getContinent()).isEqualTo(statistic.getContinent());
            assertThat(actual.getCountry()).isEqualTo(statistic.getCountry());
            assertThat(actual.getDeathsPerMillion()).isEqualTo(statistic.getDeathsPerMillion());
            assertThat(actual.getNewCases()).isEqualTo(statistic.getNewCases());
            assertThat(actual.getNewCritical()).isEqualTo(statistic.getNewCritical());
            assertThat(actual.getNewDeaths()).isEqualTo(statistic.getNewDeaths());
            assertThat(actual.getPopulation()).isEqualTo(statistic.getPopulation());
            assertThat(actual.getRecovered()).isEqualTo(statistic.getRecovered());
            assertThat(actual.getTestsPerMillion()).isEqualTo(statistic.getTestsPerMillion());
            assertThat(actual.getTime()).isEqualTo(statistic.getTime());
            assertThat(actual.getTotalCases()).isEqualTo(statistic.getTotalCases());
            assertThat(actual.getTotalDeaths()).isEqualTo(statistic.getTotalDeaths());
            assertThat(actual.getTotalTests()).isEqualTo(statistic.getTotalTests());

    
            verify(httpClient, times(1)).doHttpGet(anyString());
        }
    
    
    }