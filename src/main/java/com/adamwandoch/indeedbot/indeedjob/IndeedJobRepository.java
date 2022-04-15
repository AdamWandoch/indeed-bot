package com.adamwandoch.indeedbot.indeedjob;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Adam Wandoch
 */

public interface IndeedJobRepository extends CrudRepository<IndeedJob, String> {
}
