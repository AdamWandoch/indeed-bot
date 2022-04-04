package com.adamwandoch.indeedbot.controller;

import com.adamwandoch.indeedbot.model.IndeedJob;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.adamwandoch.indeedbot.IndeedBotApplication.cachedJobs;

/**
 * @author Adam Wandoch
 */

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "<font size=5>Home Page</font>" +
                "<br>Available endpoints:" +
                "<br> /jobs" +
                "<br> /job/{id}" +
                "Total jobs cached on startup: " + cachedJobs.size();
    }

    @GetMapping("/jobs")
    public List<IndeedJob> getJobs() {
        return cachedJobs;
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        //displays details of a record at specified index
        return cachedJobs.get(index);
//        if (index >= cachedJobs.size()) return "Job with a given index doesn't exist";
//        IndeedJob job = cachedJobs.get(index);
//        return job.getTitle() +
//                "<br>" + job.getCompany() +
//                "<br>" + job.getIndeedId() +
//                "<br>" + job.getLink();
    }
}
