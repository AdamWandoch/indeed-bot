package com.adamwandoch.indeedbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Adam Wandoch
 */

@RestController
public class PingController {

    private final String PING_ENDPOINT_URL_HEROKU = "https://indeed-bot.herokuapp.com/ping";
    private final String PING_ENDPOINT_URL_AWS = "https://ru4umr3xja.eu-west-1.awsapprunner.com/ping";
    private final Logger LOGGER = LoggerFactory.getLogger(IndeedBotApplication.class);

    @GetMapping("/ping")
    public String ping() {
        return "PING SUCCESSFUL";
    }

    void pingAll() {
        //combines ping operations to multiple instances
        ping(PING_ENDPOINT_URL_HEROKU);
        ping(PING_ENDPOINT_URL_AWS);
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
