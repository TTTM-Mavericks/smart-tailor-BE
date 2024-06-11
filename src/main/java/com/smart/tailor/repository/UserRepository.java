package com.smart.tailor.repository;


import com.smart.tailor.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query(value = "select u.* from users u where u.email = ?1", nativeQuery = true)
    User getByEmail(String email);

    @Query(value = "select u.* from users u where u.phone_number = ?1", nativeQuery = true)
    User getByPhoneNumber(String phoneNumber);
}
