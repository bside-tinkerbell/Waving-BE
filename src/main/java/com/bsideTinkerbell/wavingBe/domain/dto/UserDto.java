package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

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
    private String convertPasswordToSalt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // PBKDF2 구현을 통한 비밀번호 Salting
        // https://www.baeldung.com/java-password-hashing
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return new String(hash);
    }


    private String convertPasswordToSHA512(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword);
    }

    public LoginEntity toLoginEntity(Long userId) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return LoginEntity.builder()
                .userId(userId)
                .salt(this.convertPasswordToSalt(this.password))
                .password(this.convertPasswordToSHA512(this.password))
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
