package com.adamwandoch.indeedbot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Adam Wandoch
 */

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "[PING SUCCESSFUL]";
    }
}
