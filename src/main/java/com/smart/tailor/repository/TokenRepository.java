package com.smart.tailor.repository;


import com.smart.tailor.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = "select t.* from token t join users u on t.userid = u.userid " +
            "where u.userid = ?1 and (t.expired = 'false' or t.revoked = 'false')", nativeQuery = true)
    List<Token> findAllValidTokenByUser(Integer userID);

    Optional<Token> findByToken(String token);
}