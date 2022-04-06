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

    // tag to find job id in html code
    private final String JOB_ID_LINE_PREFIX = "<a id=\"job_";

    // tag to find job title in html code
    private String JOB_TITLE_PREFIX = "<span title=\"";

    // suffix to dispose closing html tag after job title
    private String JOB_TITLE_SUFFIX = "</span>";

    // tag to find company name in html code
    private final String COMPANY_NAME_PREFIX = "<span class=\"companyName\">";
    private final String COMPANY_NAME_PREFIX2 = "rel=\"noopener\">";

    //suffix to dispose closing html tag after company name
    private final String COMPANY_NAME_SUFFIX = "</span>";

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
                job.setIndeedId(line.substring(startIndex + 11, startIndex + 27));
                job.setLink(makeLink(job.getIndeedId()));

                // populate job title field
                while (!line.substring(startIndex).startsWith(JOB_TITLE_PREFIX)) {
                    startIndex++;
                }
                int endIndex = startIndex + JOB_TITLE_PREFIX.length();
                while (!line.substring(endIndex).startsWith("\"")) {
                    endIndex++;
                }
                job.setTitle(line.substring(startIndex + JOB_TITLE_PREFIX.length(), endIndex));

                // populate company name field
                while (!line.substring(startIndex).startsWith(COMPANY_NAME_PREFIX) &&
                       !line.substring(startIndex).startsWith(COMPANY_NAME_PREFIX2)) {
                    startIndex++;
                }
                endIndex = startIndex + COMPANY_NAME_PREFIX.length();
                while (!line.substring(endIndex).startsWith("<")) {
                    endIndex++;
                }
                job.setCompany(line.substring(startIndex + COMPANY_NAME_PREFIX.length(), endIndex));

                jobList.add(job);
            }

        }
        return jobList.stream().distinct().collect(Collectors.toList()); //distinct not working yet
    }

    private List<String> getRawHtml(String webAddress) {
        // get raw html response from indeed using start parameter for query, it changes the page
        // going over 15 times in case there was 15 pages
        List<String> rawHtmlLines = new ArrayList<>();
        for (int i = 0; i < 150; i += 10) {
            try {
                URL url = new URL(webAddress + i);
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = "";
                while (line != null) {
                    line = inputStream.readLine();
                    rawHtmlLines.add(line);
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

    /** !!! methods from previous versions below !!! */

//
//    public List<String> getRawHtml(String webAddress, boolean flag) {
//        // get raw html response from indeed
//        List<String> rawHtmlLines = new ArrayList<>();
//        try {
//            URL url = new URL(webAddress);
//            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
//            String line = "";
//            while (line != null) {
//                line = inputStream.readLine();
//                rawHtmlLines.add(line);
//            }
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            System.out.println("Interrupted exception: " + e.getMessage());
//        }
//        catch (MalformedURLException e) {
//            System.out.println("URL malformed: " + e.getMessage());
//        }
//        catch (IOException e) {
//            System.out.println("IOException: " + e.getMessage());
//        }
//
//        return rawHtmlLines;
//    }
//
//    public List<String> getHitLines(List<String> list, String searchString) {
//        //return a list of lines containing searchString
//        return list.stream()
//                .filter(l -> l != null)
//                .filter(l -> l.contains(searchString))
//                .collect(Collectors.toList());
//    }
//
//    public List<String> extractJobIds(List<String> list, String searchString) {
//        //extracting distinct job ids
//        return list.stream()
//                .filter(line -> line != null)
//                .map(line -> {
//                    int index = line.indexOf(searchString);
//                    return line.substring(index + 11, index + 27);
//                })
//                .distinct()
//                .collect(Collectors.toList());
//    }
//
//    public List<IndeedJob> getUpdatedJobs() {
//        //returns a list of updated jobs by parsing html received from indeed search query
//        List<IndeedJob> jobList = new ArrayList<>();
//        List<String> raw = getRawHtml(INDEED_QUERY_URL);
//        List<String> hits = getHitLines(raw, JOB_ID_PREFIX);
//        List<String> jobIds = extractJobIds(hits, JOB_ID_PREFIX);
//        jobIds.forEach(id -> jobList.add(getActiveJob(id)));
//
//        return jobList;
//    }
//
//    public IndeedJob getActiveJob(String indeedId) {
//        List<String> lines = getRawHtml(makeLink(indeedId), false);
//        IndeedJob job = new IndeedJob();
//        job.setIndeedId(indeedId);
//        job.setLink(makeLink(indeedId));
//
//        String title = lines.stream()
//                .filter(line -> line != null)
//                .filter(line -> line.contains("<title>"))
//                .collect(Collectors.toList())
//                .get(0);
//        int startIndex = title.indexOf("<title>") + 7;
//        int endIndex = title.indexOf("-") - 1;
//        job.setTitle(title.substring(startIndex, endIndex));
//
//        String company = lines.stream()
//                .filter(line -> line != null)
//                .filter(line -> line.contains("og:description"))
//                .collect(Collectors.toList())
//                .get(0);
//        startIndex = company.indexOf("og:description") + 25;
//        endIndex = company.indexOf("twitter:description") - 17;
//        job.setCompany(company.substring(startIndex, endIndex));
//
//        return job;
//    }
}
