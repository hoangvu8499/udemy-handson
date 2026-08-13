package com.fullstack.notification.sender;

import com.fullstack.notification.entity.NotificationOutbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mock sender: gui notification bang cach ghi log.
 */
@Slf4j
@Component
public class LogNotificationSender implements NotificationSender {

    @Override
    public void send(NotificationOutbox outbox) {
        log.info("[MOCK-{}] To: {} | Subject: {} | Content: {}",
                outbox.getChannel(), outbox.getRecipient(), outbox.getSubject(), outbox.getContent());
    }
}
