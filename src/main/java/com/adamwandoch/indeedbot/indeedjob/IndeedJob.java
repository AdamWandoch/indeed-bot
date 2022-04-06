package com.adamwandoch.indeedbot.indeedjob;

/**
 * @author Adam Wandoch
 */

public class IndeedJob {

    private String indeedId;
    private String link;
    private String title;
    private String company;

    @Override
    public String toString() {
        return "[IndeedID]: " + indeedId +
                "\n\t[Title]: " + title +
                "\n\t[Company]: " + company +
                "\n\t[Link]: " + link + "\n";
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
