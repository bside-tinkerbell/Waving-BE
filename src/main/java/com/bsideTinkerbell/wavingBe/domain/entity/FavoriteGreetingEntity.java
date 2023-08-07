package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "user", name = "favorite_greeting", indexes = {
        @Index(name = "unique_favorite_greeting", columnList = "user_id, greeting_id", unique = true)
})
public class FavoriteGreetingEntity {
    @Id
    @Column(name = "favorite_greeting_id", columnDefinition = "int unsigned comment '회원 인사말 즐겨 찾기 고유 아이디'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteGreetingId;
    @Column(name = "user_id", nullable = false, columnDefinition = "int unsigned comment '회원 고유 아이디'")
    private Long userId;
    @Column(name = "greeting_id", nullable = false,columnDefinition = "int unsigned comment '인사말 고유 아이디'")
    private Long greetingId;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false
            , columnDefinition = "datetime default current_timestamp comment '즐겨찾기 추가 시간'"
    )
    private LocalDateTime createdAt;

    @Builder
    public FavoriteGreetingEntity(Long favoriteGreetingId, Long userId, Long greetingId) {
        this.favoriteGreetingId = favoriteGreetingId;
        this.userId= userId;
        this.greetingId = greetingId;
    }
}
