package com.theodo.albeniz.controller;

import com.theodo.albeniz.services.messaging.MessagingService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message")

public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(@Qualifier("SlackSvc") MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping()
    @Hidden
    public void sendMessage(@RequestParam(name = "message", required = true) String query){
        messagingService.sendMessage(query);
    }
}
