package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 마이페이지 내 정보 수정 페이지 진입 시 응답 DTO
 */
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponseDto {
    private String name;            // 이름
    private String email;           // 이메일
    private String cellphone;       // 전화번호
    private LocalDate birthday;     // 생년월일

    @Builder
    public UserInfoResponseDto(UserEntity userEntity, PersonalAuthenticationEntity personalAuthenticationEntity) {
        this.name = personalAuthenticationEntity.getLastName() + personalAuthenticationEntity.getFirstName();
        this.email = userEntity.getUsername();
        this.cellphone = personalAuthenticationEntity.getCellphone();
        this.birthday = personalAuthenticationEntity.getBirthday();
    }
}
