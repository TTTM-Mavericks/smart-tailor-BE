package com.smart.tailor.service;



import com.smart.tailor.entities.Token;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenService {
    Optional<Token> findByToken(String token);
    void saveToken(Token token);
    boolean findTokenWithNotExpiredAndNotRevoked(String token);
    List<Token> findAllValidTokenByUser(UUID userID);
    void revokeAllUserTokens(List<Token> tokens);
}
