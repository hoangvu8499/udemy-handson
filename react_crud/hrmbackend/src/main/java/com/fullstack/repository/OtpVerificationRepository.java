package com.fullstack.repository;

import com.fullstack.entity.OtpPurpose;
import com.fullstack.entity.OtpStatus;
import com.fullstack.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByPhoneNumberAndPurposeAndStatusOrderByIdDesc(
            String phoneNumber, OtpPurpose purpose, OtpStatus status);

    /** Vo hieu hoa cac OTP PENDING cu truoc khi sinh OTP moi */
    @Modifying
    @Query("UPDATE OtpVerification o SET o.status = 'EXPIRED' " +
            "WHERE o.phoneNumber = :phoneNumber AND o.purpose = :purpose AND o.status = 'PENDING'")
    int expirePendingOtps(@Param("phoneNumber") String phoneNumber, @Param("purpose") OtpPurpose purpose);
}
