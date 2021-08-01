package com.mypros.corona.controller;
import java.util.List;

import com.mypros.corona.models.LocationStats;
import com.mypros.corona.service.CoronaVirusDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    
    @Autowired
    private CoronaVirusDataService dataService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("locationStats",dataService.getLocationStats() );
         List<LocationStats> locationStats = dataService.getLocationStats();
         int sum = locationStats.stream().mapToInt(stat-> stat.getLatestTotal()).sum();
         model.addAttribute("totalCases",sum);

       
        return "home";
    }
}
