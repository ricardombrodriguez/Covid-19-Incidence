package com.hw1.app.covid_service.resolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.hw1.app.covid_service.connection.HttpClient;
import com.hw1.app.covid_service.model.CacheStatus;
import com.hw1.app.covid_service.model.Request;
import com.hw1.app.covid_service.model.Statistic;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        JSONArray jsonArray = getJsonArray(uriBuilder);

        for (int i = 0; i < jsonArray.size(); i++) {
            allCountries.add((String) jsonArray.get(i));
        }

        logger.info("[RapidApiResolver] Returned all {0} countries", jsonArray.size());

        return allCountries;
    
    }

    public List<Statistic> getCountryHistory(String country) throws URISyntaxException, IOException, ParseException {

        URIBuilder uriBuilder = new URIBuilder(BASE_URL + "history?country=" + country);

        JSONArray jsonArray = getJsonArray(uriBuilder);

        List<Statistic> countryHistory = new ArrayList<Statistic>();

        LocalDate currentDate = null;

        List<Statistic> dayStatistics = new ArrayList<Statistic>();

        // Iterating each day/statistic
        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject stat = (JSONObject) jsonArray.get(i);

            Statistic newStatistic = parseStatistic(stat);
            if (newStatistic.getNewCases() == null) newStatistic.setNewCases(0);

            LocalDate statisticDate = LocalDate.from(newStatistic.getTime());

            // Choose the best statistic for each day (ignoring redundancy and low fidelity stats) - the one with the most new cases
            if (currentDate == null) {
                currentDate = statisticDate;
                dayStatistics.add(newStatistic);
            } else if (currentDate.equals(statisticDate)) {
                dayStatistics.add(newStatistic);
            } else {
                Statistic choosenStatistic = dayStatistics.stream().max(Comparator.comparing(Statistic::getNewCases)).orElseThrow(NoSuchElementException::new);
                countryHistory.add(choosenStatistic);
                currentDate = statisticDate;
                dayStatistics.clear();
                dayStatistics.add(newStatistic);

            }
        }

        return countryHistory;
        
    }

    public Request getCountryIntervalHistory(String country, Date initial, Date end) throws URISyntaxException, IOException, ParseException {

        LocalDate initialDate = initial.toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();

        LocalDate endDate = end.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

        long timeDifference = end.getTime() - initial.getTime();
        int fetchDays = (int) (timeDifference / (1000 * 60 * 60* 24));

        List<Statistic> countryHistory = getCountryHistory(country);

        countryHistory.removeIf((Statistic stat) -> stat.getTime().isBefore(initialDate) || stat.getTime().isAfter(endDate));

        Request req = new Request(new Date(System.currentTimeMillis()), country, fetchDays, initialDate, countryHistory, CacheStatus.MISS);

        return req;
        
    }

    public JSONArray getJsonArray(URIBuilder uriBuilder) throws IOException, URISyntaxException, ParseException {

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());

        JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);

        return (JSONArray) obj.get("response");

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
