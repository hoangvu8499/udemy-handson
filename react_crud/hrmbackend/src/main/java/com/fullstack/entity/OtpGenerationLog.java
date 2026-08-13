package com.fullstack.entity;

import com.fullstack.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Bang log chi tiet moi lan sinh OTP (mock data cho phan generate OTP).
 * Luu y: bang nay duoc PARTITION BY RANGE (created_at) nen DDL nam trong
 * src/main/resources/schema.sql (Hibernate khong tao duoc partitioned table),
 * KHONG khai bao index o day de tranh Hibernate tao trung.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "otp_generation_logs")
public class OtpGenerationLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OtpChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OtpPurpose purpose;

    /** OTP dang plain text — chi de mock/test, production phai bo hoac mask */
    @Column(name = "otp_code", nullable = false, length = 6)
    private String otpCode;

    @Column(name = "otp_expires_at", nullable = false)
    private LocalDateTime otpExpiresAt;

    @Column(name = "request_ip", length = 45)
    private String requestIp;

    @Column(name = "user_agent", length = 255)
    private String userAgent;
}
