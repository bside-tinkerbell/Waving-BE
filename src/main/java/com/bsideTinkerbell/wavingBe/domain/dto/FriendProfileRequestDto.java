package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.FriendProfileEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FriendProfileRequestDto {

		// 지인 프로필 Request
		private Long contactId;                // 연락처 id (friend 테이블)
		private Long friendProfileId;          // 연락처 프로필 고유id
		@Min(0)
		@Max(1)
		private int isFavorite;                // 즐겨찾기
		private String name;                   // 지인 이름
		private LocalDate birthday;            // 지인 생일
		private int contactCycle;              // 지인 연락 주기
		private String phoneNumber;            // 지인 연락처
		private LocalDate recentContactDate;   // 최근연락일(마지막연락일)

		public FriendProfileEntity toProfileEntity() {
				return FriendProfileEntity.builder()
								.friendProfileId(friendProfileId)
								.contactId(contactId)
								.isFavorite(isFavorite)
								.name(name)
								.birthday(birthday)
								.contactCycle(contactCycle)
								.phoneNumber(phoneNumber)
								.recentContactDate(recentContactDate)
								.build();
		}

		@Builder
		public FriendProfileRequestDto(Long friendProfileId, Long contactId, int isFavorite, String name, LocalDate birthday, int contactCycle, String phoneNumber, LocalDate recentContactDate) {
				this.friendProfileId = friendProfileId;
				this.contactId = contactId;
				this.isFavorite = isFavorite;
				this.name = name;
				this.birthday = birthday;
				this.contactCycle = contactCycle;
				this.phoneNumber = phoneNumber;
				this.recentContactDate = recentContactDate;
		}
}
