package com.smart.tailor.entities;

import com.smart.tailor.enums.TokenType;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends AuditEntity implements Serializable {
    @Id
    @Column(name = "token_id", nullable = false, unique = true)
    private String tokenID;

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

    @PrePersist
    private void prePersist() {
        this.tokenID = Utilities.generateCustomPrimaryKey();
    }
}