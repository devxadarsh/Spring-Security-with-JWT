package org.devx.springscurityclient.event.listner;

import lombok.extern.slf4j.Slf4j;
import org.devx.springscurityclient.entity.User;
import org.devx.springscurityclient.event.RegistrationCompleteEvent;
import org.devx.springscurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        // Create the verification token for the User with Link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.verificationTokenForUserService(token, user);

        // Sent mail to User
        String url = event.getApplicationUrl()
                + "/verifyRegistration?token="
                + token;

        // sendVerificationEmail()
        log.info("Click the link below to verify registration: {}", url);
    }
}
