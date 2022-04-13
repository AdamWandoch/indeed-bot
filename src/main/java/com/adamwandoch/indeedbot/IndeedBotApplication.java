package com.adamwandoch.indeedbot;

import com.adamwandoch.indeedbot.indeedjob.IndeedJobService;
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

    @Autowired
    private IndeedJobService indeedJobService;

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(initialDelayString ="${ping.delay}", fixedDelayString = "${ping.delay}")
    void ping() {
        // keeps the free dyno awake on Heroku sending a request in a regular time interval
        System.out.println("[PINGING INITIALIZED]");
        try {
            URL url = new URL(PING_ENDPOINT_URL);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while (line != null) {
                line = inputStream.readLine();
                if (line != null) {
                    System.out.println("[RESPONSE LINE RECEIVED] : " + line);
                }
            }
            inputStream.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelayString = "${initial.update.delay}", fixedDelayString = "${update.delay}")
    void update() {
        indeedJobService.updateJobs();
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
