package com.smart.tailor.service;

import com.smart.tailor.entities.User;
import com.smart.tailor.entities.VerificationToken;
import com.smart.tailor.enums.TypeOfVerification;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenService {
    Optional<VerificationToken> findByToken(UUID token);

    VerificationToken findByUserID(UUID userID);

    void saveUserVerificationToken(User user, UUID token, TypeOfVerification typeOfVerification);

    VerificationToken generateNewVerificationToken(String userEmail);

    VerificationToken findVerificationTokenByUserEmail(String userEmail);

    void enableVerificationToken(VerificationToken verificationToken);
}
