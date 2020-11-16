package com.dpworld.coronavirustracker.services;

import com.dpworld.coronavirustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        List<LocationStats> stats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(VIRUS_DATA_URL))
                                .build();

        HttpResponse<String> httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());

        StringReader csvReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {
            LocationStats stat = new LocationStats();

             stat.setState(record.get("Province/State"));
             stat.setCountry(record.get("Country/Region"));
             stat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
             stat.setDiffFromPreviousDay(Integer.parseInt(record.get(record.size()-1)) -Integer.parseInt(record.get(record.size()-2)));
//            System.out.println(stat);
             stats.add(stat);

        }

        this.allStats=stats;

        
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
}
