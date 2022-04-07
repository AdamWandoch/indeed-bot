package com.adamwandoch.indeedbot.indeedjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Adam Wandoch
 */

@RestController
public class IndeedJobController {

    @Autowired
    private IndeedJobService indeedJobService;

    @GetMapping("/")
    public String home() {
        return "<font size=5>Indeed.ie Java job listings in Cork" +
                "<br>Testing endpoints:" +
                "<br> /jobs (returns a json list of all job resources)" +
                "<br> /job/{index} (returns a single job resource)" +
                "<br> /update (scans Indeed.ie again and updates jobs resource cache)" +
                "<br>Total jobs cached: " + indeedJobService.getCachedJobs().size();
    }

    @GetMapping("/jobs")
    public List<IndeedJob> getJobs() {
        return indeedJobService.getCachedJobs();
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        return indeedJobService.getJob(index);
    }

    @GetMapping("/update")
    public String update() {
        indeedJobService.update();
        return "<font size=5>Updated job list size: " + indeedJobService.getCachedJobs().size();
    }
}
