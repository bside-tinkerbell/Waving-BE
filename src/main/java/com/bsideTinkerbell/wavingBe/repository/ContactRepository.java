package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
	Optional<ContactEntity> findByUserId(Long userId);
//	void deleteByUserId(Long userId);
}
