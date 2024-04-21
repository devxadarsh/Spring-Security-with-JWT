package org.devx.springscurityclient.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.devx.springscurityclient.entity.User;
import org.devx.springscurityclient.entity.VerificationToken;
import org.devx.springscurityclient.event.RegistrationCompleteEvent;
import org.devx.springscurityclient.model.PasswordModel;
import org.devx.springscurityclient.model.UserModel;
import org.devx.springscurityclient.repository.PasswordResetTokenRepository;
import org.devx.springscurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Successfully registered!";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationtoken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verified successfully!";
        }else if(result.equalsIgnoreCase("expired")){
            return "Verification Token expired!";
        } else {
            return "Invalid verification token!";
        }
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        // we can use both method for resend verification using event or using another method
//        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        // earlier we used event not let us use creating new method in itself
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification token has been sent!";

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;

    }

    @PostMapping("/savePassword")
    private String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid token";
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if(user.isPresent()){
            userService.changePasswordWithToken(user.get(), passwordModel.getNewPassword(),token);
            return "Password reset Successfully";
        }else {
            return "Invalid token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())){
            userService.changePassword(user, passwordModel.getNewPassword());
            return "Invalid old password or email";
        }else{

            // Save new Password
            userService.changePassword(user, passwordModel.getNewPassword());
            return "Password changed successfully!";
        }
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;

        // sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

        // sendVerificationEmail()
        log.info("Click the link to verify your account: {}", url);

    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
