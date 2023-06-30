package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
//import java.security.spec.KeySpec;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private int gatherAgree;
    @Email
    private String username;
    @NotBlank
    private String password;
    @Min(0)
    @Max(1)
    private int loginType;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String birthday;
    @Min(0)
    @Max(1)
    private int sex;
    @NotBlank
    private String cellphone;

    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .userId(userId)
                .username(username)
                .loginType(loginType)
                .build();
    }

    public LoginEntity toLoginEntity(Long userId, String salt, String sha512Password) {
        return LoginEntity.builder()
                .userId(userId)
                .salt(salt)
                .password(sha512Password)
                .build();
    }

    public PersonalAuthenticationEntity toSelfAuthenticationEntity(Long userId) {
        return PersonalAuthenticationEntity.builder()
                .userId(userId)
                .gatherAgree(this.gatherAgree)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthday(this.birthday)
                .sex(this.sex)
                .cellphone(this.cellphone)
                .build();
    }
}
