package org.devx.springscurityclient.repository;

import org.devx.springscurityclient.entity.User;
import org.devx.springscurityclient.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
//    void deleteByExpiryDateBefore(LocalDateTime oneMinuteAgo);
}
