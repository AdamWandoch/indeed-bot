package com.adamwandoch.indeedbot.indeedjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Adam Wandoch
 */

@RestController
public class IndeedJobController {

    @Autowired
    private IndeedJobService indeedJobService;

    @GetMapping("/jobs")
    public IndeedJobsWrapper getJobs() {
        return new IndeedJobsWrapper(indeedJobService.getCachedJobs());
    }
    
    @GetMapping("/jobs/sort/id")
    public IndeedJobsWrapper getJobsSortId() {
        return new IndeedJobsWrapper(indeedJobService.getCachedJobsSortId());
    }

    @GetMapping("/jobs/sort/title")
    public IndeedJobsWrapper getJobsSortTitle() {
        return new IndeedJobsWrapper(indeedJobService.getCachedJobsSortTitle());
    }

    @GetMapping("/jobs/sort/company")
    public IndeedJobsWrapper getJobsSortCompany() {
        return new IndeedJobsWrapper(indeedJobService.getCachedJobsSortCompany());
    }

    @GetMapping("/job/{index}")
    public IndeedJobsWrapper job(@PathVariable(value = "index") int index) {
        return new IndeedJobsWrapper(indeedJobService.getJobByIndex(index));
    }

    @GetMapping("/jobs/title/{keyword}")
    public IndeedJobsWrapper getJobsByTitle(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByTitle(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/title/{keyword}/sort/id")
    public IndeedJobsWrapper getJobsByTitleSortId(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByTitleSortId(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/title/{keyword}/sort/title")
    public IndeedJobsWrapper getJobsByTitleSortTitle(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByTitleSortTitle(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/title/{keyword}/sort/company")
    public IndeedJobsWrapper getJobsByTitleSortCompany(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByTitleSortCompany(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/company/{keyword}")
    public IndeedJobsWrapper getJobsByCompany(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByCompany(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/company/{keyword}/sort/id")
    public IndeedJobsWrapper getJobsByCompanySortId(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByCompanySortID(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/company/{keyword}/sort/title")
    public IndeedJobsWrapper getJobsByCompanySortTitle(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByCompanySortTitle(keyword.toLowerCase()));
    }

    @GetMapping("/jobs/company/{keyword}/sort/company")
    public IndeedJobsWrapper getJobsByCompanySortCompany(@PathVariable(value = "keyword") String keyword) {
        return new IndeedJobsWrapper(indeedJobService.getJobsByCompanySortCompany(keyword.toLowerCase()));
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
