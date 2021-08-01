package com.mypros.corona.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.mypros.corona.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.StringReader;
import java.net.URI;

@Slf4j
@Service
@Data
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> locationStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "30 * * * * *")
    public void fetchData(){

         List<LocationStats> newStats = new ArrayList<>();

        try {
            HttpClient client =HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(VIRUS_DATA_URL))
            .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            StringReader csvReader = new StringReader( httpResponse.body().toString());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
            for (CSVRecord record : records) {
                String state = record.get("Province/State");
                log.info(state);
                LocationStats locStats = new LocationStats();
                locStats.setState(record.get("Province/State"));
                locStats.setCountry(record.get("Country/Region"));
                locStats.setLatestTotal(Integer.parseInt(record.get(record.size() - 1)));
                log.info("location stats=========>"+locStats);
                newStats.add(locStats);
                this.locationStats = newStats;
            }
  


        } catch (Exception e) {
           log.error(e.getMessage(), e);
        } 
    }
}
