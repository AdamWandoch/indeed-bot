package com.adamwandoch.indeedbot.indeedjob;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Adam Wandoch
 */

@Service
public class IndeedJobService {

    public IndeedJobService() {
    }

    public IndeedJobService(String PAGE_COUNT_PREFIX, String JOB_TITLE_PREFIX, List<IndeedJob> cachedJobs) {
        this.PAGE_COUNT_PREFIX = PAGE_COUNT_PREFIX;
        this.JOB_TITLE_PREFIX = JOB_TITLE_PREFIX;
        this.cachedJobs = cachedJobs;
    }

    // prefix for pages count
    private String PAGE_COUNT_PREFIX = "Page 1 of ";

    // tag to find job id in html code
    private final String JOB_ID_LINE_PREFIX = "]= {jk:'";

    // tag to find job title in html code
    private String JOB_TITLE_PREFIX = ",title:'";

    // tag to find company name in html code
    private final String COMPANY_NAME_PREFIX = ",srcname:'";

    // prefix to find company location, currently not in use (INDEED_QUERY_URL returns only jobs with Cork as location)
    private final String COMPANY_LOCATION_PREFIX = "<div class=\"companyLocation\">";

    // query with "Java" keyword, Cork location and sorted from newest
    private final String INDEED_QUERY_URL = "https://ie.indeed.com/jobs?q=java&l=cork&sort=date&filter=0&start=";

    private List<IndeedJob> cachedJobs = getUpdatedJobs();

    public List<IndeedJob> getCachedJobs() {
        return cachedJobs;
    }

    // returns a list of IndeedJob objects by parsing html
    private List<IndeedJob> getUpdatedJobs() {
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

                // populate indeedId and link fields
                int startIndex = line.indexOf(JOB_ID_LINE_PREFIX);
                job.setIndeedId(line.substring(startIndex + 8, startIndex + 24));
                job.setLink(makeLink(job.getIndeedId()));

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
        System.out.println("[Job list count] : " + jobList.stream().distinct().count());
        return jobList.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getRawHtml(String webAddress) {
        // get raw html response from indeed using start parameter for query, it changes the page
        // going over 15 times in case there was 15 pages
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

                // find out how many result pages
                if (limit == Integer.MAX_VALUE) {
                    String pageCount = rawHtmlLines.stream()
                            .filter(l -> l != null)
                            .filter(l -> l.contains(PAGE_COUNT_PREFIX))
                            .collect(Collectors.toList()).get(0);

                    int startIndex = pageCount.indexOf(PAGE_COUNT_PREFIX) + PAGE_COUNT_PREFIX.length();
                    int endIndex = startIndex;
                    while (!pageCount.substring(endIndex).startsWith(" ")) {
                        endIndex++;
                    }
                    limit = Integer.parseInt(pageCount.substring(startIndex, endIndex).replaceAll("\\D", ""));
                    System.out.println("[Limit value] : " + limit);
                }

                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                System.out.println("Interrupted exception: " + e.getMessage());
            }
            catch (MalformedURLException e) {
                System.out.println("URL malformed: " + e.getMessage());
            }
            catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }

        return rawHtmlLines;
    }

    private String makeLink(String jobId) {
        //returns a job link for a given jobId
        return "https://ie.indeed.com/viewjob?jk=".concat(jobId);
    }

    public void update() {
        cachedJobs = getUpdatedJobs();
    }

    public IndeedJob getJob(int index) {
        return cachedJobs.get(index);
    }
}
