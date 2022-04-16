package com.hw1.app.covid_service.resolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.hw1.app.covid_service.connection.HttpClient;
import com.hw1.app.covid_service.model.Statistic;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;


/*
https://rapidapi.com/api-sports/api/covid-193/
*/

@Component
public class RapidApiResolver {

    private static Logger logger = LogManager.getLogger(RapidApiResolver.class);

    @Autowired
    private HttpClient httpClient;

    public List<String> getCountries() throws URISyntaxException, IOException, ParseException {

        logger.info("[RapidApiResolver] Retrieving all countries...");

        List<String> allCountries = new ArrayList<String>();

        URIBuilder uriBuilder = new URIBuilder("https://covid-193.p.rapidapi.com/countries");

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        for (int i = 0; i < jsonArray.size(); i++) {
            allCountries.add((String) jsonArray.get(i));
        }

        logger.info(apiResponse);

        logger.info("[RapidApiResolver] Returned all " + jsonArray.size() + " countries");

        return allCountries;
    
    }

    public List<Statistic> getAllCountriesStatistics() throws URISyntaxException, IOException, ParseException {

        logger.info("[RapidApiResolver] Retrieving all country statistics...");
        
        URIBuilder uriBuilder = new URIBuilder("https://covid-193.p.rapidapi.com/statistics");
        
        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        List<Statistic> countriesStatistics = new ArrayList<Statistic>();

        // Iterating each country
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject stat = (JSONObject) jsonArray.get(i);

            Statistic newStatistic = parseStatistic(stat);

            countriesStatistics.add(newStatistic);

        }

        logger.info("[RapidApiResolver] Countries statistics (for today):");

        logger.info(countriesStatistics);
        System.out.println(countriesStatistics);

        return countriesStatistics;

    }

    public Optional<Statistic> getCountryStatistics(String country) throws URISyntaxException, IOException, ParseException {

        logger.info("[RapidApiResolver] Retrieving " + country + " statistics...");

        URIBuilder uriBuilder = new URIBuilder("https://covid-193.p.rapidapi.com/statistics?country=" + country);
        
        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        System.out.println(apiResponse);

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        if (jsonArray.isEmpty()) {

            return Optional.empty();

        } else {

            JSONObject stat = (JSONObject) jsonArray.get(0);

            Statistic newStatistic = parseStatistic(stat);

            return Optional.of(newStatistic);

        }

    }

    public List<Statistic> getCountryHistoryByDate(String country, Date day) throws URISyntaxException, IOException, ParseException {

        SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String formatted_date = date_formatter.format(day);  

        URIBuilder uriBuilder = new URIBuilder("https://covid-193.p.rapidapi.com/history?country=" + country + "&day=" + formatted_date);

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        System.out.println(apiResponse);

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        List<Statistic> countryHistory = new ArrayList<Statistic>();

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject stat = (JSONObject) jsonArray.get(i);

            Statistic newStatistic = parseStatistic(stat);

            countryHistory.add(newStatistic);

        }

        System.out.println(countryHistory);

        return countryHistory;
        
    }

    public List<Statistic> getCountryHistory(String country) throws URISyntaxException, IOException, ParseException {

        URIBuilder uriBuilder = new URIBuilder("https://covid-193.p.rapidapi.com/history?country=" + country);

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        System.out.println(apiResponse);

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        List<Statistic> countryHistory = new ArrayList<Statistic>();

        // Iterating each day/statistic
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject stat = (JSONObject) jsonArray.get(i);

            Statistic newStatistic = parseStatistic(stat);

            countryHistory.add(newStatistic);

        }

        return countryHistory;
        
    }

    public Statistic parseStatistic(JSONObject stat) {

        String cntry = (String) stat.get("country");
        String continent = (String) stat.get("continent");
        Integer population = (stat.get("population") != null) ? ((Long) stat.get("population")).intValue() : null;

        JSONObject cases = (JSONObject) stat.get("cases");
        String cases_str = (cases.get("new") != null) ? (String) cases.get("new") : null;
        Integer new_cases = (cases_str != null) ? Integer.parseInt(cases_str.replace("+", "")) : null;
        Integer recovered = (cases.get("recovered") != null) ? ((Long) cases.get("recovered")).intValue() : null;
        Integer total_cases = (cases.get("total") != null) ? ((Long) cases.get("total")).intValue() : null;
        Integer new_critical = (cases.get("critical") != null) ? ((Long) cases.get("critical")).intValue() : null;
        Integer active = (cases.get("active") != null) ? ((Long) cases.get("active")).intValue() : null;
        Double cases_per_million = (cases.get("1M_pop") != null) ? Double.parseDouble((String) cases.get("1M_pop")) : null;

        JSONObject tests = (JSONObject) stat.get("tests");
        Integer total_tests = (tests.get("total") != null) ? ((Long) tests.get("total")).intValue() : null;
        Double tests_per_million = (tests.get("1M_pop") != null) ? Double.parseDouble((String) tests.get("1M_pop")) : null;

        JSONObject deaths = (JSONObject) stat.get("deaths");
        String deaths_str = (deaths.get("new") != null) ? (String) deaths.get("new") : null;
        Integer new_deaths = (deaths_str != null) ? Integer.parseInt(deaths_str.replace("+", "")) : null;
        Integer total_deaths = (deaths.get("total") != null) ? ((Long) deaths.get("total")).intValue() : null;
        Double deaths_per_million = (deaths.get("1M_pop") != null) ? Double.parseDouble((String) deaths.get("1M_pop")) : null;

        String time_str = ((String) stat.get("time")).replace("+00:00","");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDate date = LocalDate.parse(time_str, formatter);
        System.out.println(time_str);

        return new Statistic(cntry, continent, population, new_cases, recovered, total_cases, new_critical, active, cases_per_million, total_tests, tests_per_million, new_deaths, total_deaths, deaths_per_million, date);

    }

}
