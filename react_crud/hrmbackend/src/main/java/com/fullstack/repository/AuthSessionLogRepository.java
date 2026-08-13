package com.fullstack.repository;

import com.fullstack.entity.AuthSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthSessionLogRepository extends JpaRepository<AuthSessionLog, Long> {
}
