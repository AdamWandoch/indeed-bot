package com.adamwandoch.indeedbot.indeedjob;

import java.util.Objects;

/**
 * @author Adam Wandoch
 */

public class IndeedJob {

    private String indeedId;
    private String url;
    private String title;
    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIndeedId() {
        return indeedId;
    }

    public void setIndeedId(String indeedId) {
        this.indeedId = indeedId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "IndeedJob{" +
                "indeedId='" + indeedId + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndeedJob indeedJob = (IndeedJob) o;
        return Objects.equals(indeedId, indeedJob.indeedId) && Objects.equals(url, indeedJob.url) && Objects.equals(title, indeedJob.title) && Objects.equals(company, indeedJob.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indeedId, url, title, company);
    }
}
