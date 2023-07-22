package com.bsideTinkerbell.wavingBe.repository;

import com.bsideTinkerbell.wavingBe.domain.entity.FriendProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendProfileRepository extends JpaRepository<FriendProfileEntity, Long> {

		List<FriendProfileEntity> findAllByContactId(Long contactId);

		// 즐겨찾기 & userId
		List<FriendProfileEntity> findAllByContactIdAndIsFavorite(Long contactId, int isFavorite);

		// 즐겨찾기 & userId Cnt
		Long countAllByContactIdAndIsFavorite(Long contactId, int isFavorite);

		// 지인 프로필 view
		Optional<FriendProfileEntity> findOneByFriendProfileId(Long profileId);

}
