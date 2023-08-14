package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * 마이페이지 회원 정보 수정 DTO
 * 내 정보, 전화번호, 비밀번호는 각각 독립적으로 update
 * 정보 재설정할때 모든 필드가 들어오지 않기 때문에 일단 jakarta validation annotation은 미포함
 * 전화번호 재설정할때 PersonalAuthenticationVerificationDto 사용하면 되기 때문에 필요시 비밀번호, 사용자정보로도 분리 가능
 */
@NoArgsConstructor
@Getter
public class UserInfoRequestDto {
    private Long id;                    // PK (user_id)
    private String name;                // (내 정보) 이름
    private String email;               // (내 정보) 이메일 (username)
    private String cellphone;           // 전화번호
    private LocalDate birthday;         // (내 정보) 생년월일
    private String currentPassword;     // 현재 비밀번호
    private String newPassword;         // 새로운 비밀번호
    private String newPasswordCheck;    // 새로운 비밀번호 확인
}
