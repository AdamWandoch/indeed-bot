package com.adamwandoch.indeedbot.indeedjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Adam Wandoch
 */

@Service
public class IndeedJobService {

    private static final Logger LOG = LoggerFactory.getLogger(IndeedJobService.class);

    @Autowired
    private IndeedJobRepository indeedJobRepository;
    private final IndeedScanner indeedScanner = IndeedScanner.getInstance();

    public IndeedJobService() {
    }

    public void scanIndeed() {
        // scans indeed and saves retrieved results to database
        LOG.info("SCANNING INITIALIZED");
        List<IndeedJob> updatedJobs = indeedScanner.getUpdatedJobs();
        if (updatedJobs.size() > 0) {
            LOG.info("CACHED JOB LIST WITH " + updatedJobs.size() + " RECORDS");
            indeedJobRepository.saveAll(updatedJobs);
        } else {
            LOG.info("JOB LIST SIZE == 0, SCANNING UNSUCCESSFUL");
        }
    }
}
