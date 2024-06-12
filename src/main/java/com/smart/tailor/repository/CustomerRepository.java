package com.smart.tailor.repository;

import com.smart.tailor.entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Customer (customer_id, gender, date_of_birth, address, province, district, ward, number_of_violations, create_date, last_modified_date) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, 0, CURRENT_TIMESTAMP, NULL)", nativeQuery = true)
    void createCustomer(UUID customerID, Boolean gender, Date dateOfBirth, String address,
                        String province, String district, String ward);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Customer SET gender = ?1, date_of_birth = ?2, " +
            "address = ?3, province = ?4, district = ?5, " +
            "ward = ?6, last_modified_date = CURRENT_TIMESTAMP " +
            "WHERE customer_id = ?7", nativeQuery = true)
    void updateCustomer(Boolean gender, Date dateOfBirth, String address,
                        String province, String district, String ward, UUID customerID);
}
