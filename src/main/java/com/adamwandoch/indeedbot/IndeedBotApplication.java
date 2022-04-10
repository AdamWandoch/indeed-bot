package com.adamwandoch.indeedbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
public class IndeedBotApplication {

    public static final String PING_ENDPOINT_URL = "https://indeed-bot.herokuapp.com/ping";

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(fixedDelay = 5000L)
    void ping() {
        // attempts to keep the free dyno awake on heroku sending a request in a regular time interval
        System.out.println("PINGING...");
        try {
            URL url = new URL(PING_ENDPOINT_URL);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while (line != null) {
                line = inputStream.readLine();
                System.out.println("[RESPONSE LINE RECEIVED]: " + line);
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

}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class SchedulingConfiguration {

}
