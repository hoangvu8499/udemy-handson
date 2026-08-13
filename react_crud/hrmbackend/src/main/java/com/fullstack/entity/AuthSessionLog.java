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

/**
 * Log chi tiet moi phien login/logout.
 * Bang PARTITION BY RANGE (created_at) — DDL nam trong schema.sql,
 * khong khai bao index o entity de tranh Hibernate tao trung.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "auth_session_logs")
public class AuthSessionLog extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuthSessionAction action;

    /** JWT ID cua access token gan voi phien */
    @Column(length = 36)
    private String jti;

    @Column(name = "request_ip", length = 45)
    private String requestIp;

    @Column(name = "user_agent", length = 255)
    private String userAgent;
}
