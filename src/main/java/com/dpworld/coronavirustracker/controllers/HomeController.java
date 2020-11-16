package com.dpworld.coronavirustracker.controllers;

import com.dpworld.coronavirustracker.models.LocationStats;
import com.dpworld.coronavirustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model theModel)
    {
        theModel.addAttribute("locationStats",coronaVirusDataService.getAllStats());

        int totalReportedCases = 0;
        for(LocationStats stat:coronaVirusDataService.getAllStats())
        {
            totalReportedCases+=stat.getLatestTotalCases();
        }

        theModel.addAttribute("totalReportedCases",totalReportedCases);
        return "home";
    }
}
