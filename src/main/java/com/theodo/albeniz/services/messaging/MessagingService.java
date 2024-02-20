package com.theodo.albeniz.services.messaging;

import org.springframework.stereotype.Service;

@Service
public interface MessagingService {
    void sendMessage(String aMessage);
}
