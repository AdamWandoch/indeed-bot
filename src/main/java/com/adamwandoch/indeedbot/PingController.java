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

    private static final Logger LOG = LoggerFactory.getLogger(PingController.class);

    @GetMapping("/ping")
    public String ping_endpoint() {
        return "PING SUCCESSFUL";
    }

    public void notifyAPI(String url_string) {
        LOG.info("NOTIFYING API FOR RECORDS RELOAD");
        LOG.info("PINGING URL : " + url_string);
        try {
            URL url = new URL(url_string);
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
