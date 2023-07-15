package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.GreetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GreetingRepository extends JpaRepository<GreetingEntity, Long> {
    Optional<GreetingEntity> findFirstByOrderByGreeting();
    List<GreetingEntity> findByCategory(String categoryName);
}
