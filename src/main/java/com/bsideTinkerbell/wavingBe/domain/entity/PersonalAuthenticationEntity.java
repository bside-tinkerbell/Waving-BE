package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "user", name = "self_authentication")
public class PersonalAuthenticationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long selfAuthenticationId;
    private Long userId;
    private int gatherAgree;
    private String firstName;
    private String lastName;
    private String birthday;
    private int sex;
    private String cellphone;
    @LastModifiedDate
    @Column(name = "auth_date", nullable = false, updatable = true, columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime authDate;

    @Builder
    public PersonalAuthenticationEntity(
            Long selfAuthenticationId
            , Long userId
            , int gatherAgree
            , String firstName
            , String lastName
            , String birthday
            , int sex
            , String cellphone
    ) {
        this.selfAuthenticationId = selfAuthenticationId;
        this.userId = userId;
        this.gatherAgree = gatherAgree;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.sex = sex;
        this.cellphone = cellphone;
    }
}
