package com.adamwandoch.indeedbot.controller;

import com.adamwandoch.indeedbot.model.IndeedJob;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.adamwandoch.indeedbot.IndeedBotApplication.cachedJobs;

/**
 * @author Adam Wandoch
 */

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "<font size=5>Home Page</font><br>" +
                "Try using /jobs endpoint...<br>" +
                "Total jobs cached on startup: " + cachedJobs.size();
    }

    @GetMapping("/jobs")
    public String jobs() {
        //attempts to display all active jobs with their details
        String body = "";
//        cachedJobs.forEach(job -> {
//            body.concat("<p>*********************</p>");
//            body.concat("<p>Title: " + job.getTitle() + "</p>");
//            body.concat("<p>Company: " + job.getCompany() + "</p>");
//            body.concat("<p>IndeedID: " + job.getIndeedId() + "</p>");
//            body.concat("<p>LINK: " + job.getLink() + "</p>");
//            body.concat("<p>*********************</p>");
//        });
        body += "Total count: " + cachedJobs.size() + "<br>";
        return body;
    }

    @GetMapping("/job/{index}")
    public String job(@PathVariable(value = "index") int index) {
        //displays details of a record at specified index
        if (index >= cachedJobs.size()) return "Job with a given index doesn't exist";
        IndeedJob job = cachedJobs.get(index);
        return job.getTitle() +
                "<br>" + job.getCompany() +
                "<br>" + job.getIndeedId() +
                "<br>" + job.getLink();
    }
}
