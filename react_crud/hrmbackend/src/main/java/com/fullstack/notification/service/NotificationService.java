package com.fullstack.notification.service;

import com.fullstack.notification.entity.NotificationChannel;
import com.fullstack.notification.entity.NotificationOutbox;
import com.fullstack.notification.entity.NotificationStatus;
import com.fullstack.notification.repository.NotificationOutboxRepository;
import com.fullstack.notification.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationOutboxRepository outboxRepository;
    private final NotificationSender notificationSender;

    @Transactional
    public void sendOtp(NotificationChannel channel, String recipient, String otpCode, long expiresInSeconds) {
        send(channel, recipient, "Your OTP Code",
                String.format("Ma OTP cua ban la %s, het han sau %d giay.", otpCode, expiresInSeconds));
    }

    /** Canh bao he thong (vd: doi soat ton kho lech) — mock gui email qua outbox/log */
    @Transactional
    public void sendEmailAlert(String recipient, String subject, String content) {
        send(NotificationChannel.EMAIL, recipient, subject, content);
    }

    private void send(NotificationChannel channel, String recipient, String subject, String content) {
        NotificationOutbox outbox = new NotificationOutbox();
        outbox.setChannel(channel);
        outbox.setRecipient(recipient);
        outbox.setSubject(subject);
        outbox.setContent(content);
        outbox.setStatus(NotificationStatus.PENDING);
        outboxRepository.save(outbox);

        try {
            notificationSender.send(outbox);
            outbox.setStatus(NotificationStatus.SENT);
            outbox.setSentAt(LocalDateTime.now());
        } catch (Exception ex) {
            log.error("Send notification failed, outboxId={}", outbox.getId(), ex);
            outbox.setStatus(NotificationStatus.FAILED);
            outbox.setRetryCount(outbox.getRetryCount() + 1);
            outbox.setErrorMessage(ex.getMessage());
        }
        outboxRepository.save(outbox);
    }
}
