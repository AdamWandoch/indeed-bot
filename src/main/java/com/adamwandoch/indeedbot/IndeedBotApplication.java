package com.adamwandoch.indeedbot;

import com.adamwandoch.indeedbot.model.IndeedJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class IndeedBotApplication {

	public static List<IndeedJob> cachedJobs = SearchUtils.getAllActiveJobs();

	public static void main(String[] args) {
		SpringApplication.run(IndeedBotApplication.class, args);
	}

}
