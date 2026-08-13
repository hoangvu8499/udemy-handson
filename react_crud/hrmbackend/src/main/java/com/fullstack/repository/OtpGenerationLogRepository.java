package com.fullstack.repository;

import com.fullstack.entity.OtpGenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpGenerationLogRepository extends JpaRepository<OtpGenerationLog, Long> {
}
