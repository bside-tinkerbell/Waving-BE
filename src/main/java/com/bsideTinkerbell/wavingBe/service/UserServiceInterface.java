package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.UserDto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 회원 서비스 구현을 위한 인터페이스
 */
public interface UserServiceInterface {
    public void createUser(UserDto userDto) throws Exception, NoSuchAlgorithmException, InvalidKeySpecException;
//    public void updateUser();
//    public void deleteUser();
}
