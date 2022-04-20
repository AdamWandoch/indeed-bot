package com.adamwandoch.indeedbot.indeedjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Adam Wandoch
 */

@Service
public class IndeedJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndeedJobService.class);

    @Autowired
    private IndeedJobRepository indeedJobRepository;

    public IndeedJobService() {
    }

    public IndeedJobService(List<IndeedJob> cachedJobs) {
        this.cachedJobs = cachedJobs;
    }

    // prefix for pages count
    private final String PAGE_COUNT_PREFIX = "Page 1 of ";

    // prefix to find job id in html code
    private final String JOB_ID_LINE_PREFIX = "]= {jk:'";

    // prefix to find job title in html code
    private final String JOB_TITLE_PREFIX = ",title:'";

    // prefix to find company name in html code
    private final String COMPANY_NAME_PREFIX = ",cmp:'";

    // query with "software" keyword, Cork location and sorted from newest
    private final String INDEED_QUERY_URL = "https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=";

    private List<IndeedJob> cachedJobs = new ArrayList<>();

    private List<IndeedJob> getUpdatedJobs() {
        // returns a list of IndeedJob objects by parsing html retrieved from Indeed.ie
        LOGGER.info("UPDATING INITIALIZED");
        List<IndeedJob> jobList = new ArrayList<>();
        List<String> htmlLines = getRawHtml(INDEED_QUERY_URL).stream()
                .filter(l -> l != null)
                .filter(l -> l.contains(JOB_ID_LINE_PREFIX) ||
                        l.contains(JOB_TITLE_PREFIX) ||
                        l.contains(COMPANY_NAME_PREFIX))
                .collect(Collectors.toList());

        // parse all lines one by one looking for details to create an IndeedJob object
        for (int i = 0; i < htmlLines.size(); i++) {
            String line = htmlLines.get(i);

            if (line.contains(JOB_ID_LINE_PREFIX)) {
                IndeedJob job = new IndeedJob();

                // populate indeedId and url fields
                int startIndex = line.indexOf(JOB_ID_LINE_PREFIX);
                job.setIndeedId(line.substring(startIndex + 8, startIndex + 24));
                job.setUrl(generateJobUrl(job.getIndeedId()));

                // populate company name field
                while (!line.substring(startIndex).startsWith(COMPANY_NAME_PREFIX)) {
                    startIndex++;
                }
                int endIndex = startIndex + COMPANY_NAME_PREFIX.length();
                while (!line.substring(endIndex).startsWith("'")) {
                    endIndex++;
                }
                job.setCompany(line.substring(startIndex + COMPANY_NAME_PREFIX.length(), endIndex));

                // populate job title field
                while (!line.substring(startIndex).startsWith(JOB_TITLE_PREFIX)) {
                    startIndex++;
                }
                endIndex = startIndex + JOB_TITLE_PREFIX.length();
                while (!line.substring(endIndex).startsWith("'")) {
                    endIndex++;
                }
                job.setTitle(line.substring(startIndex + JOB_TITLE_PREFIX.length(), endIndex));

                jobList.add(job);
            }
        }

        return jobList.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getRawHtml(String webAddress) {
        // get raw html response from indeed using start parameter for query
        List<String> rawHtmlLines = new ArrayList<>();
        int limit = Integer.MAX_VALUE;
        for (int i = 0; i < limit; i += 10) {
            try {
                URL url = new URL(webAddress + i);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = "";
                while (line != null) {
                    line = inputStream.readLine();
                    rawHtmlLines.add(line);
                }
                inputStream.close();

                // find out how many result pages
                if (limit == Integer.MAX_VALUE) {
                    String pageCount = rawHtmlLines.stream()
                            .filter(l -> l != null)
                            .peek(l -> LOGGER.info("HTML PEEK : " + l))
                            .filter(l -> l.contains(PAGE_COUNT_PREFIX))
                            .collect(Collectors.toList()).get(0);

                    int startIndex = pageCount.indexOf(PAGE_COUNT_PREFIX) + PAGE_COUNT_PREFIX.length();
                    int endIndex = startIndex;
                    while (!pageCount.substring(endIndex).startsWith(" ")) {
                        endIndex++;
                    }
                    limit = Integer.parseInt(pageCount.substring(startIndex, endIndex).replaceAll("\\D", ""));
                    LOGGER.info("TOTAL RECORDS FOUND : " + limit);
                }
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                LOGGER.error("Interrupted exception : " + e.getMessage());
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                LOGGER.error("URL malformed : " + e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e) {
                LOGGER.error("IOException : " + e.getMessage());
                e.printStackTrace();
            }
        }

        return rawHtmlLines;
    }

    private String generateJobUrl(String jobId) {
        return "https://ie.indeed.com/viewjob?jk=".concat(jobId);
    }

    public void cacheAndStoreJobs() {
        // runs a cycle of restoring jobs cache from database and updating cache
        // and database with records retrieved from Indeed.ie if there are new ones
        indeedJobRepository.findAll().forEach(job -> {
            if (!cachedJobs.contains(job)) cachedJobs.add(job);
        });
        List<IndeedJob> updatedJobs = getUpdatedJobs();
        updatedJobs.forEach(job -> {
            if (!cachedJobs.contains(job)) cachedJobs.add(job);
        });

        if (cachedJobs.size() > 0) {
            LOGGER.info("CACHED JOB LIST WITH " + cachedJobs.size() + " RECORDS");
            indeedJobRepository.saveAll(cachedJobs);
        } else {
            LOGGER.info("JOB LIST SIZE == 0, CACHING UNSUCCESSFUL");
        }
    }

    public List<IndeedJob> getJobByIndex(int index) {
        return List.of(cachedJobs.get(index));
    }

    public List<IndeedJob> getCachedJobs() {
        return cachedJobs;
    }

    public List<IndeedJob> getCachedJobsSortId() {
        return cachedJobs.stream()
                .sorted(Comparator.comparing(IndeedJob::getIndeedId))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getCachedJobsSortTitle() {
        return cachedJobs.stream()
                .sorted(Comparator.comparing(IndeedJob::getTitle))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getCachedJobsSortCompany() {
        return cachedJobs.stream()
                .sorted(Comparator.comparing(IndeedJob::getCompany))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByTitle(String keyword) {
        return getCachedJobs().stream()
                .filter(j -> j.getTitle().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByTitleSortId(String keyword) {
        return getJobsByTitle(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getIndeedId))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByTitleSortTitle(String keyword) {
        return getJobsByTitle(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getTitle))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByTitleSortCompany(String keyword) {
        return getJobsByTitle(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getCompany))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByCompany(String keyword) {
        return getCachedJobs().stream()
                .filter(j -> j.getCompany().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByCompanySortID(String keyword) {
        return getJobsByCompany(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getIndeedId))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByCompanySortTitle(String keyword) {
        return getJobsByCompany(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getTitle))
                .collect(Collectors.toList());
    }

    public List<IndeedJob> getJobsByCompanySortCompany(String keyword) {
        return getJobsByCompany(keyword).stream()
                .sorted(Comparator.comparing(IndeedJob::getCompany))
                .collect(Collectors.toList());
    }

    public long getDBTableSize() {
        return indeedJobRepository.count();
    }
}
