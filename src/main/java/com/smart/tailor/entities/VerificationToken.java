package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfVerification;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_token")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken implements Serializable {
    @Id
    @Column(name = "verification_token_id")
    @UuidGenerator
    private UUID verificationTokenID;

    private UUID token;

    private LocalDateTime expirationDateTime;

    @Enumerated(EnumType.STRING)
    private TypeOfVerification typeOfVerification;

    private boolean isEnabled;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
