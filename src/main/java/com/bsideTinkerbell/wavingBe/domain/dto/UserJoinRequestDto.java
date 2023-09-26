package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.SecureRandom;
import java.time.LocalDate;
//import java.security.spec.KeySpec;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserJoinRequestDto {
    private Long userId;
    @Min(0)
    @Max(1)
    private int gatherAgree;
    @Email
    private String username;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    @Min(0)
    @Max(1)
    private int loginType;
//    @NotBlank
//    private String firstName;
//    @NotBlank
//    private String lastName;
    @NotBlank
    @Length(min=2, max=8)
    private String name;
    @NotNull
    private LocalDate birthday;
    @Min(0)
    @Max(1)
    private int sex;
    @NotBlank
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}")
    private String cellphone;



    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .userId(userId)
                .username(username)
                .loginType(loginType)
                .build();
    }

    public LoginEntity toLoginEntity(Long userId, String salt, String sha256Password) {
        return LoginEntity.builder()
                .userId(userId)
                .salt(salt)
                .password(sha256Password)
                .build();
    }

    public PersonalAuthenticationEntity toSelfAuthenticationEntity(Long userId) {
        return PersonalAuthenticationEntity.builder()
                .userId(userId)
                .gatherAgree(this.gatherAgree)
//                .firstName(this.firstName)
//                .lastName(this.lastName)
                .firstName(this.name.substring(1))
                .lastName(this.name.substring(0, 1))
                .birthday(this.birthday)
//                .sex(this.sex)
                .cellphone(this.cellphone)
                .build();
    }
}
