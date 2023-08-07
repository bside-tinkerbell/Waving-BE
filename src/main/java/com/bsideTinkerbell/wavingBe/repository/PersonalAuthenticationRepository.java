package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalAuthenticationRepository extends JpaRepository<PersonalAuthenticationEntity, Long> {
    PersonalAuthenticationEntity findByUserId(Long id);
}
