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

    @Query(value = "select user_id, email, password, full_name, language, phone_number, provider, role_id, last_modified_date, create_date, user_status from users where email = ?1", nativeQuery = true)
    User getByEmail(String email);
}
