package com.theodo.albeniz.services.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SlackSvc")
public class SlackMessagingService implements MessagingService {
    @Override
    public void sendMessage(String aMessage) {
        log.info("Send message {} through Slack", aMessage);
    }
}
