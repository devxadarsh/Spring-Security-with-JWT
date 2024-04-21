package org.devx.springscurityclient.repository;

import org.devx.springscurityclient.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime oneMinuteAgo);

}
