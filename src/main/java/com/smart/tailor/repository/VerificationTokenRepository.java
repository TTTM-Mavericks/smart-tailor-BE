package com.smart.tailor.repository;

import com.smart.tailor.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(UUID token);

    @Query(value = "select * from verification_token where user_id = ?1", nativeQuery = true)
    VerificationToken findByUserID(UUID userID);
}
