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

    private static final Logger LOG = LoggerFactory.getLogger(IndeedBotApplication.class);
    private final String PING_ENDPOINT_URL_AWS_API = "https://3hvy3ei8qx.eu-west-1.awsapprunner.com/ping";

    @Autowired
    private IndeedJobService indeedJobService;

    @Autowired
    private PingController pingController;

    public static void main(String[] args) {
        SpringApplication.run(IndeedBotApplication.class, args);
    }

    @Scheduled(initialDelayString = "${initial.update.delay}", fixedDelayString = "${update.delay}")
    void scanIndeed() {
        // scans Indeed.ie
        indeedJobService.scanIndeed();
        pingController.notifyAPI(PING_ENDPOINT_URL_AWS_API);
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
