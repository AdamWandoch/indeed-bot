package com.adamwandoch.indeedbot.controller;

import com.adamwandoch.indeedbot.SearchUtils;
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
        return "<font size=7>Indeed.ie Java job listings in Cork" +
                "<br>Testing endpoints:" +
                "<br> /jobs (returns a json list of all job resources)" +
                "<br> /job/{index} (returns a single job resource)" +
                "<br> /update (scans Indeed.ie again and updates jobs resource cache)" +
                "<br>Total jobs cached: " + cachedJobs.size();
    }

    @GetMapping("/jobs")
    public List<IndeedJob> getJobs() {
        return cachedJobs;
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        return cachedJobs.get(index);
    }

    @GetMapping("/update")
    public String update() {
        cachedJobs = SearchUtils.updateAllActiveJobs();
        return "<font size=7>Updated job list size: " + cachedJobs.size();
    }
}
