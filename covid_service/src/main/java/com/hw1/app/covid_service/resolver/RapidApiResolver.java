package com.hw1.app.covid_service.resolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

@Component
public class RapidApiResolver {

    private static Logger logger = LogManager.getLogger(RapidApiResolver.class);

    private static final String BASE_URL = "https://covid-193.p.rapidapi.com/"; 

    @Autowired
    private HttpClient httpClient;

    public List<String> getCountries() throws URISyntaxException, IOException, ParseException {

        logger.info("[RapidApiResolver] Retrieving all countries...");

        List<String> allCountries = new ArrayList<String>();

        URIBuilder uriBuilder = new URIBuilder(BASE_URL + "countries");

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        for (int i = 0; i < jsonArray.size(); i++) {
            allCountries.add((String) jsonArray.get(i));
        }

        logger.info(apiResponse);

        logger.info("[RapidApiResolver] Returned all {0} countries", jsonArray.size());

        return allCountries;
    
    }

    public List<Statistic> getCountryHistoryByDate(String country, Date day) throws URISyntaxException, IOException, ParseException {

        SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");  
        String formatted_date = date_formatter.format(day);  

        URIBuilder uriBuilder = new URIBuilder(BASE_URL + "history?country=" + country + "&day=" + formatted_date);

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        JSONArray jsonArray = (JSONArray) obj.get("response");

        List<Statistic> countryHistory = new ArrayList<Statistic>();

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject stat = (JSONObject) jsonArray.get(i);

            Statistic newStatistic = parseStatistic(stat);

            countryHistory.add(newStatistic);

        }

        return countryHistory;
        
    }

    public List<Statistic> getCountryHistory(String country) throws URISyntaxException, IOException, ParseException {

        URIBuilder uriBuilder = new URIBuilder(BASE_URL + "history?country=" + country);

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

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

    public List<Statistic> getCountryIntervalHistory(String country, Date initial, Date end) throws URISyntaxException, IOException, ParseException {

        LocalDate initialDate = initial.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

        LocalDate endDate = end.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

        List<Statistic> countryHistory = getCountryHistory(country);

        for (Statistic stat : countryHistory) {
            if (stat.getTime().isBefore(initialDate) || stat.getTime().isAfter(endDate)) {
                countryHistory.remove(stat);
            }
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

        return new Statistic(cntry, continent, population, new_cases, recovered, total_cases, new_critical, active, cases_per_million, total_tests, tests_per_million, new_deaths, total_deaths, deaths_per_million, date);

    }

}
