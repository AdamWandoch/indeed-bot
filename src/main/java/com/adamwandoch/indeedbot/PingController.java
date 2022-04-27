package com.adamwandoch.indeedbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger LOG = LoggerFactory.getLogger(PingController.class);
    private final String RELOAD_ENDPOINT_URL_AWS_API = "https://3hvy3ei8qx.eu-west-1.awsapprunner.com/reload";

    @Value("${CRON}")
    private String cron;

    @GetMapping("/ping")
    public String ping_endpoint() {
        return "PING SUCCESSFUL";
    }

    @GetMapping("/cron")
    public String show_cron() {
        return "CRON value: " + cron;
    }

    public void notifyAPI() {
        LOG.info("NOTIFYING API INITIALIZED");
        LOG.info("PINGING URL : " + RELOAD_ENDPOINT_URL_AWS_API);
        try {
            URL url = new URL(RELOAD_ENDPOINT_URL_AWS_API);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            String message = "";
            while (message != null) {
                message = inputStream.readLine();
                if (message != null) {
                    LOG.info("RESPONSE RECEIVED : " + message);
                }
            }
            inputStream.close();
        }
        catch (MalformedURLException e) {
            LOG.error("MALFORMED URL EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e) {
            LOG.error("IOEXCEPTION EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
