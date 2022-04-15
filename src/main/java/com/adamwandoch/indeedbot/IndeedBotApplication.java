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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
public class IndeedBotApplication {

    public static final String PING_ENDPOINT_URL = "https://indeed-bot.herokuapp.com/ping";

    private static final Logger LOGGER = LoggerFactory.getLogger(IndeedBotApplication.class);

    @Autowired
    private IndeedJobService indeedJobService;

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(initialDelayString ="${ping.delay}", fixedDelayString = "${ping.delay}")
    void ping() {
        // keeps the free dyno awake on Heroku sending a request in a regular time interval
        LOGGER.info("PINGING INITIALIZED");
        try {
            URL url = new URL(PING_ENDPOINT_URL);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String message = "";
            while (message != null) {
                message = inputStream.readLine();
                if (message != null) {
                    LOGGER.info("RESPONSE RECEIVED : " + message);
                } else {
                    LOGGER.warn("NULL RECEIVED, PING FAILED");
                }
            }
            inputStream.close();
        }
        catch (MalformedURLException e) {
            LOGGER.error("MALFORMED URL EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            LOGGER.error("IOEXCEPTION EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelayString = "${initial.update.delay}", fixedDelayString = "${update.delay}")
    void refreshJobsData() {
        // initializes main function: load cache from database, retrieve new list from Indeed.ie, update database
        indeedJobService.cacheAndStoreJobs();
    }

}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class SchedulingConfiguration {}

@Configuration
class StaticViewConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/homepage.html");
    }
}
