package org.devx.springscurityclient.config;

import org.devx.springscurityclient.repository.UserRepository;
import org.devx.springscurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataCleanupTask {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataCleanupTask(VerificationTokenRepository verificationTokenRepository, UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 1 * 60 * 1000) // 30 minutes
    @Transactional
    public void deleteOldData() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        verificationTokenRepository.deleteByExpiryDateBefore(oneMinuteAgo);
//        userRepository.deleteByExpiryDateBefore(oneMinuteAgo);
    }
}
