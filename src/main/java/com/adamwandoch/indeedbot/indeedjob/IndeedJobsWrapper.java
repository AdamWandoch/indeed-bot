package com.adamwandoch.indeedbot.indeedjob;

import java.util.List;

/**
 * @author Adam Wandoch
 * This wraps returned records to future-proof for backwards compatibility
 * so returned JSON root is an object and not a list
 */

public class IndeedJobsWrapper {

    private long listSize;
    private List<IndeedJob> recordList;

    public IndeedJobsWrapper(List<IndeedJob> jobList) {
        this.recordList = jobList;
        this.listSize = recordList.size();
    }

    public long getListSize() {
        return listSize;
    }

    public void setListSize(long listSize) {
        this.listSize = listSize;
    }

    public List<IndeedJob> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<IndeedJob> recordList) {
        this.recordList = recordList;
    }
}
