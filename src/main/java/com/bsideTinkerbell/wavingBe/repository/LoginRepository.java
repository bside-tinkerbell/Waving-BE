package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
    Optional<LoginEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
