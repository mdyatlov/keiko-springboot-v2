package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.services.LibraryService;
import com.theodo.albeniz.services.messaging.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("message")
public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(@Qualifier("SlackSvc") MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping()
    public void sendMessage(@RequestParam(name = "message", required = true) String query){
        messagingService.sendMessage(query);
    }


}
