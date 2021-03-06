package com.adamwandoch.indeedbot.indeedjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Adam Wandoch
 */

public class IndeedScanner {

    private static final Logger LOG = LoggerFactory.getLogger(IndeedScanner.class);
    private static final IndeedScanner instance = new IndeedScanner();

    // prefix for pages count
    private final String PAGE_COUNT_PREFIX = "Page 1 of ";

    // prefix to find job id in html code
    private final String JOB_ID_PREFIX = "]= {jk:'";

    // prefix to find job title in html code
    private final String JOB_TITLE_PREFIX = ",title:'";

    // prefix to find company name in html code
    private final String COMPANY_NAME_PREFIX = ",cmp:'";

    // query with "software" keyword, Cork location and sorted from newest
    private final String INDEED_QUERY_URL = "https://ie.indeed.com/jobs?q=software&l=cork&sort=date&filter=0&start=";

    private IndeedScanner() {
    }

    public static IndeedScanner getInstance() {
        return instance;
    }

    public List<IndeedJob> getUpdatedJobs() {
        // returns a list of IndeedJob objects by parsing html retrieved from Indeed.ie
        List<IndeedJob> jobList = new ArrayList<>();
        List<String> htmlLines = getRawHtml(INDEED_QUERY_URL).stream()
                .filter(Objects::nonNull)
                .filter(l -> l.contains(JOB_ID_PREFIX) ||
                        l.contains(JOB_TITLE_PREFIX) ||
                        l.contains(COMPANY_NAME_PREFIX))
                .collect(Collectors.toList());

        // parse all lines one by one looking for details to create an IndeedJob object
        for (String line : htmlLines) {
            if (line.contains(JOB_ID_PREFIX)) {
                IndeedJob job = new IndeedJob();

                // populate indeedId and url fields
                int startIndex = line.indexOf(JOB_ID_PREFIX);
                job.setIndeedId(line.substring(startIndex + JOB_ID_PREFIX.length(), startIndex + 24));
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
                            .filter(Objects::nonNull)
                            .peek(l -> LOG.info("HTML PEEK : " + l))
                            .filter(l -> l.contains(PAGE_COUNT_PREFIX))
                            .collect(Collectors.toList()).get(0);

                    int startIndex = pageCount.indexOf(PAGE_COUNT_PREFIX) + PAGE_COUNT_PREFIX.length();
                    int endIndex = startIndex;
                    while (!pageCount.substring(endIndex).startsWith(" ")) {
                        endIndex++;
                    }
                    limit = Integer.parseInt(pageCount.substring(startIndex, endIndex).replaceAll("\\D", ""));
                    LOG.info("TOTAL RECORDS FOUND : " + limit);
                }
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                LOG.error("Interrupted exception : " + e.getMessage());
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                LOG.error("URL malformed : " + e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e) {
                LOG.error("IOException : " + e.getMessage());
                e.printStackTrace();
            }
        }

        return rawHtmlLines;
    }

    private String generateJobUrl(String jobId) {
        return "https://ie.indeed.com/viewjob?jk=".concat(jobId);
    }
}
