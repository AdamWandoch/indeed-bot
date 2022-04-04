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
        return "<font size=7>Indeed.ie Java Jobs" +
                "<br>Testing endpoints:" +
                "<br> /jobs" +
                "<br> /job/{index}" +
                "<br>Total jobs cached on startup: " + cachedJobs.size() + "</font>";
    }

    @GetMapping("/jobs")
    public List<IndeedJob> getJobs() {
        return cachedJobs;
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        return cachedJobs.get(index);
    }
}
