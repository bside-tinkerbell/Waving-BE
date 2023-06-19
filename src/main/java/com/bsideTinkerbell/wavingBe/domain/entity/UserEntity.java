package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", schema = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Long userId;
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;
    @Column(name = "login_type", nullable = false)
    private int loginType;
    @CreatedDate
    @Column(name = "join_date", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime joinDate;
    @LastModifiedDate
    @Column(name = "update_date", nullable = true, columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @Builder
    public UserEntity(Long userId, String username, int loginType) {
        this.userId = userId;
        this.username = username;
        this.loginType = loginType;
    }
}
