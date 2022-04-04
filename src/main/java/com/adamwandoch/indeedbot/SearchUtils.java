package com.adamwandoch.indeedbot;

import com.adamwandoch.indeedbot.model.IndeedJob;

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
 * Utilities for parsing indeed html source code
 */

public class SearchUtils {

    private SearchUtils() {
    }

    private static final String SEARCH_STRING = "<a id=\"job_";

    // query with java keyword, cork location and sorted from newest
    private static final String INDEED_QUERY_URL = "https://ie.indeed.com/jobs?q=java&l=cork&sort=date&filter=0&start=";

    public static List<String> getRawHtml(String webAddress) {
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
            } catch (InterruptedException e) {
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

    public static List<String> getRawHtml(String webAddress, boolean flag) {
        // get raw html response from indeed
        List<String> rawHtmlLines = new ArrayList<>();
        try {
            URL url = new URL(webAddress);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while (line != null) {
                line = inputStream.readLine();
                rawHtmlLines.add(line);
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception: " + e.getMessage());
        }
        catch (MalformedURLException e) {
            System.out.println("URL malformed: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return rawHtmlLines;
    }

    public static List<String> getHitLines(List<String> list, String searchString) {
        //return only lines containing searchString
        return list.stream()
                .filter(l -> l != null)
                .filter(l -> l.contains(searchString))
                .collect(Collectors.toList());
    }

    public static List<String> extractJobIds(List<String> list, String searchString) {
        //extracting distinct job ids
        return list.stream()
                .filter(line -> line != null)
                .map(line -> {
                    int index = line.indexOf(searchString);
                    return line.substring(index + 11, index + 27);
                })
                .distinct()
                .collect(Collectors.toList());
    }

    public static String makeLink(String jobId) {
        //returns a job link for a given jobId
        return "https://ie.indeed.com/viewjob?jk=".concat(jobId);
    }

    public static List<IndeedJob> updateAllActiveJobs() {
        //returns a list of all IndeedJob objects from given Indeed job id list
        List<IndeedJob> jobList = new ArrayList<>();
        List<String> raw = getRawHtml(INDEED_QUERY_URL);
        List<String> hits = getHitLines(raw, SEARCH_STRING);
        List<String> jobIds = extractJobIds(hits, SEARCH_STRING);
        jobIds.forEach(id -> jobList.add(getActiveJob(id)));

        return jobList;
    }

    public static IndeedJob getActiveJob(String indeedId) {
        List<String> lines = getRawHtml(makeLink(indeedId), false);
        IndeedJob job = new IndeedJob();
        job.setIndeedId(indeedId);
        job.setLink(makeLink(indeedId));

        String title = lines.stream()
                .filter(line -> line != null)
                .filter(line -> line.contains("<title>"))
                .collect(Collectors.toList())
                .get(0);
        int startIndex = title.indexOf("<title>") + 7;
        int endIndex = title.indexOf("-") - 1;
        job.setTitle(title.substring(startIndex, endIndex));

        String company = lines.stream()
                .filter(line -> line != null)
                .filter(line -> line.contains("og:description"))
                .collect(Collectors.toList())
                .get(0);
        startIndex = company.indexOf("og:description") + 25;
        endIndex = company.indexOf("twitter:description") - 17;
        job.setCompany(company.substring(startIndex, endIndex));

        return job;
    }


}
