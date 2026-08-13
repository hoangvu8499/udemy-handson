package com.fullstack.notification.sender;

import com.fullstack.notification.entity.NotificationOutbox;

/**
 * Interface gui notification — hien tai chi co mock (log),
 * sau nay them EmailSender/SmsSender that.
 */
public interface NotificationSender {

    void send(NotificationOutbox outbox);
}
