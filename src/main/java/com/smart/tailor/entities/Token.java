package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smart.tailor.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
@EntityListeners(AuditingEntityListener.class)
public class Token extends AuditEntity implements Serializable {

    @Id
    @Column(name = "token_id", nullable = false, unique = true)
    @UuidGenerator
    private UUID tokenID;

    @Column(name = "expired", nullable = false, unique = false)
    private boolean expired;

    @Column(name = "revoked", nullable = false, unique = false)
    private boolean revoked;

    @Column(name = "token", nullable = false, unique = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false, unique = false)
    private TokenType tokenType = TokenType.BEARER;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}