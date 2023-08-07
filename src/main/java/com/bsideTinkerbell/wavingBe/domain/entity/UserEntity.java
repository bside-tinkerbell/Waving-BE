package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
//import java.util.Collection;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "user", name = "user", indexes = {@Index(
        name = "unique_username"
        , columnList = "username"
        , unique = true
)})
public class UserEntity implements UserDetails {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "int unsigned")
    private Long userId;
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;
    @Column(name = "login_type", nullable = false)
    private int loginType;
    @CreatedDate
    @Column(name = "joined_at", nullable = false, updatable = false
            , columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime joinedAt;

    @Builder
    public UserEntity(Long userId, String username, int loginType) {
        this.userId = userId;
        this.username = username;
        this.loginType = loginType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
