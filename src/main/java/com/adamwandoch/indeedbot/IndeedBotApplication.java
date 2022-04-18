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

    public static final String PING_ENDPOINT_URL_HEROKU = "https://indeed-bot.herokuapp.com/ping";
    public static final String PING_ENDPOINT_URL_AWS = "https://ru4umr3xja.eu-west-1.awsapprunner.com/ping";

    private static final Logger LOGGER = LoggerFactory.getLogger(IndeedBotApplication.class);

    @Autowired
    private IndeedJobService indeedJobService;

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(initialDelayString ="PT5S", fixedDelayString = "${ping.delay}")
    void pingAll() {
        //combines multiple ping operations
        ping(PING_ENDPOINT_URL_HEROKU);
        ping(PING_ENDPOINT_URL_AWS);
    }

    @Scheduled(initialDelayString = "${initial.update.delay}", fixedDelayString = "${update.delay}")
    void refreshJobsData() {
        // initializes main function: load cache from database, retrieve new list from Indeed.ie, update database
        indeedJobService.cacheAndStoreJobs();
    }

    void ping(String url_string) {
        // prevents free instances from going to sleep by sending a ping request in a regular time interval
        LOGGER.info("PINGING URL : " + url_string);
        try {
            URL url = new URL(url_string);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String message = "";
            while (message != null) {
                message = inputStream.readLine();
                if (message != null) {
                    LOGGER.info("RESPONSE RECEIVED : " + message);
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
