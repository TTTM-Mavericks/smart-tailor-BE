package com.smart.tailor.service.impl;

import com.smart.tailor.entities.User;
import com.smart.tailor.entities.VerificationToken;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.repository.VerificationTokenRepository;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public Optional<VerificationToken> findByToken(UUID token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken findByUserID(UUID userID) {
        return verificationTokenRepository.findByUserID(userID);
    }

    @Override
    public void saveUserVerificationToken(User user, UUID token, TypeOfVerification typeOfVerification) {
        LocalDateTime localDateTime = LocalDateTime.now();
        VerificationToken existedVerificationToken = findByUserID(user.getUserID());
        if(existedVerificationToken == null){
            verificationTokenRepository.save(
                    VerificationToken
                            .builder()
                            .token(token)
                            .user(user)
                            .typeOfVerification(typeOfVerification)
                            .expirationDateTime(localDateTime.plusMinutes(1))
                            .build()
            );
        }
        else {
            existedVerificationToken.setTypeOfVerification(typeOfVerification);
            existedVerificationToken.setToken(token);
            existedVerificationToken.setExpirationDateTime(localDateTime.plusMinutes(1));
            verificationTokenRepository.save(existedVerificationToken);
        }
    }

    @Override
    public VerificationToken generateNewVerificationToken(UUID oldToken) {
        Optional<VerificationToken> verificationTokenOptional = findByToken(oldToken);
        VerificationToken verificationToken = verificationTokenOptional.get();
        if(verificationTokenOptional.isPresent()){
            verificationToken.setToken(UUID.randomUUID());
            verificationToken.setExpirationDateTime(LocalDateTime.now().plusMinutes(1));
            return verificationTokenRepository.save(verificationToken);
        }
        return null;
    }

    @Override
    public VerificationToken findVerificationTokenByUserEmail(String userEmail) {
        if(!Utilities.isStringNotNullOrEmpty(userEmail)){
            return null;
        }
        VerificationToken verificationToken = verificationTokenRepository.findVerificationTokenByUserEmail(userEmail);
        return generateNewVerificationToken(verificationToken.getToken());
    }
}
