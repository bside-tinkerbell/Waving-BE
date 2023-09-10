package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.FavoriteGreetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteGreetingRepository extends JpaRepository<FavoriteGreetingEntity, Long> {
//    void deleteByUserId(Long userId);
}
