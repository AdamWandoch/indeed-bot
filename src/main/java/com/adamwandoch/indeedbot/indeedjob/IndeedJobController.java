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

    @GetMapping("/jobs")
    public List<IndeedJob> getJobs() {
        return indeedJobService.getCachedJobs();
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        return indeedJobService.getJob(index);
    }

    @GetMapping("/jobs/title/{keyword}")
    public List<IndeedJob> getJobsByTitle(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByTitle(keyword.toLowerCase());
    }

    @GetMapping("/jobs/company/{keyword}")
    public List<IndeedJob> getJobsByCompany(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByCompany(keyword.toLowerCase());
    }

    @GetMapping("/update")
    public String update() {
        indeedJobService.updateJobs();
        return "<font size=5>Updated job list size: " + indeedJobService.getCachedJobs().size();
    }
}
