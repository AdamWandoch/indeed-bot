package com.adamwandoch.indeedbot.indeedjob;

import com.adamwandoch.indeedbot.SearchUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Adam Wandoch
 */

@Service
public class IndeedJobService {

    private List<IndeedJob> cachedJobs = SearchUtils.updateAllActiveJobs();

    public List<IndeedJob> getCachedJobs() {
        return cachedJobs;
    }

    public IndeedJob get(int index) {
        return cachedJobs.get(index);
    }
}
