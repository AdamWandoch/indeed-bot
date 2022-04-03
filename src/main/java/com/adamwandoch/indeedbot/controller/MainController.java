package com.adamwandoch.indeedbot.controller;

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
        return "<font size=5>Home Page</font><br>Try using /jobs endpoint...";
    }

    @GetMapping("/jobs")
    public String jobs() {
        //displays all active jobs
        String body = "";
//        cachedJobs.stream()
//                .filter(job -> job.getTitle().contains("Java"))
//                .forEach(job -> {
//                    body.concat("<p>*********************</p>");
//                    body.concat("<p>Title: " + job.getTitle() + "</p>");
//                    body.concat("<p>Company: " + job.getCompany() + "</p>");
//                    body.concat("<p>IndeedID: " + job.getIndeedId() + "</p>");
//                    body.concat("<p>LINK: " + job.getLink() + "</p>");
//                    body.concat("<p>*********************</p>");
//                });
        body = "cachedJobs.size() = " + cachedJobs.size();
        return body;
    }

    @GetMapping("/job/{index}")
    public String job(@PathVariable(value = "index") int index) {
        return cachedJobs.get(index).getTitle();
    }
}
