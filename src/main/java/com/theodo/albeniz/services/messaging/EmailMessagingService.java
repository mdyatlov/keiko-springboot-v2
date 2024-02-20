package com.theodo.albeniz.services.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("EmailSvc")
public class EmailMessagingService implements MessagingService {
    @Override
    public void sendMessage(String aMessage) {
        log.info("Send message {} through email", aMessage);
    }
}
