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
    
    @GetMapping("/jobs/sort/id")
    public List<IndeedJob> getJobsSortId() {
        return indeedJobService.getCachedJobsSortId();
    }

    @GetMapping("/jobs/sort/title")
    public List<IndeedJob> getJobsSortTitle() {
        return indeedJobService.getCachedJobsSortTitle();
    }

    @GetMapping("/jobs/sort/company")
    public List<IndeedJob> getJobsSortCompany() {
        return indeedJobService.getCachedJobsSortCompany();
    }

    @GetMapping("/job/{index}")
    public IndeedJob job(@PathVariable(value = "index") int index) {
        return indeedJobService.getJobByIndex(index);
    }

    @GetMapping("/jobs/title/{keyword}")
    public List<IndeedJob> getJobsByTitle(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByTitle(keyword.toLowerCase());
    }

    @GetMapping("/jobs/title/{keyword}/sort/id")
    public List<IndeedJob> getJobsByTitleSortId(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByTitleSortId(keyword.toLowerCase());
    }

    @GetMapping("/jobs/title/{keyword}/sort/title")
    public List<IndeedJob> getJobsByTitleSortTitle(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByTitleSortTitle(keyword.toLowerCase());
    }

    @GetMapping("/jobs/title/{keyword}/sort/company")
    public List<IndeedJob> getJobsByTitleSortCompany(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByTitleSortCompany(keyword.toLowerCase());
    }

    @GetMapping("/jobs/company/{keyword}")
    public List<IndeedJob> getJobsByCompany(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByCompany(keyword.toLowerCase());
    }

    @GetMapping("/jobs/company/{keyword}/sort/id")
    public List<IndeedJob> getJobsByCompanySortId(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByCompanySortID(keyword.toLowerCase());
    }

    @GetMapping("/jobs/company/{keyword}/sort/title")
    public List<IndeedJob> getJobsByCompanySortTitle(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByCompanySortTitle(keyword.toLowerCase());
    }

    @GetMapping("/jobs/company/{keyword}/sort/company")
    public List<IndeedJob> getJobsByCompanySortCompany(@PathVariable(value = "keyword") String keyword) {
        return indeedJobService.getJobsByCompanySortCompany(keyword.toLowerCase());
    }

    @GetMapping("/update")
    public String update() {
        indeedJobService.cacheAndStoreJobs();
        return "<font size=5>Updated job list size: " + indeedJobService.getCachedJobs().size();
    }

    @GetMapping("/size")
    public String size() {
        return "Database jobs count : " + indeedJobService.getDBTableSize();
    }
}
