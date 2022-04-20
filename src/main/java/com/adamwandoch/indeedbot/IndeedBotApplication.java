package com.adamwandoch.indeedbot;

import com.adamwandoch.indeedbot.indeedjob.IndeedJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class IndeedBotApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndeedBotApplication.class);

    @Autowired
    private IndeedJobService indeedJobService;

    @Autowired
    private PingController pingController;

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(initialDelayString = "PT5S", fixedDelayString = "${ping.delay}")
    void pingAll() {
        LOGGER.info("PING ALL INITIALIZED");
        pingController.pingAll();
    }

    @Scheduled(initialDelayString = "${initial.update.delay}", fixedDelayString = "${update.delay}")
    void refreshJobsData() {
        // initializes main function: load cache from database, retrieve new list from Indeed.ie, update database
        LOGGER.info("CACHE AND STORE INITIALIZED");
        indeedJobService.cacheAndStoreJobs();
    }
}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class SchedulingConfiguration {
}

@Configuration
class StaticViewConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/homepage.html");
    }
}
