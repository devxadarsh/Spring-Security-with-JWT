package org.devx.springscurityclient.service;

import org.devx.springscurityclient.entity.User;
import org.devx.springscurityclient.entity.VerificationToken;
import org.devx.springscurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {

    User registerUser(UserModel userModel);

    void verificationTokenForUserService(String token, User user);

    String validateVerificationtoken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    void changePasswordWithToken(User user, String newPassword, String token);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
