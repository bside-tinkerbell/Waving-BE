package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "authentication", name = "login")
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "login_id")
    private Long loginId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "salt")
    private String salt;
    @Column(name = "password")
    private String password;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = true, columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @Builder
    public LoginEntity(
            Long loginId
            , Long userId
            , String salt
            , String password
    ) {
        this.loginId = loginId;
        this.userId = userId;
        this.salt = salt;
        this.password = password;
    }
}
