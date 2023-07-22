package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.FriendProfileEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FriendProfileResponseDto {

		// 지인 프로필 Response
		private Long contactId;                // 연락처 id (friend 테이블)
		private Long friendProfileId;          // 연락처 프로필 고유id
		@Min(0)
		@Max(1)
		private int isFavorite;                // 즐겨찾기
		private String name;                   // 지인 이름
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy.MM.dd")
		private LocalDate birthday;            // 지인 생일
		private int contactCycle;              // 지인 연락 주기
		private String phoneNumber;            // 지인 연락처
		@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yy.MM.dd")
		private LocalDate recentContactDate;   // 최근연락일(마지막연락일)

		// Entity -> DTO
		@Builder
		public FriendProfileResponseDto(FriendProfileEntity profileEntity) {
				this.friendProfileId = profileEntity.getFriendProfileId();
				this.contactId = profileEntity.getContactId();
				this.isFavorite = profileEntity.getIsFavorite();
				this.name = profileEntity.getName();
				this.birthday = profileEntity.getBirthday();
				this.contactCycle = profileEntity.getContactCycle();
				this.phoneNumber = profileEntity.getPhoneNumber();
				this.recentContactDate = profileEntity.getRecentContactDate();
		}

		@Builder
		public FriendProfileResponseDto(FriendProfileRequestDto friendProfileRequestDto) {
				this.friendProfileId = friendProfileRequestDto.getFriendProfileId();
				this.contactId = friendProfileRequestDto.getContactId();
				this.isFavorite = friendProfileRequestDto.getIsFavorite();
				this.name = friendProfileRequestDto.getName();
				this.birthday = friendProfileRequestDto.getBirthday();
				this.contactCycle = friendProfileRequestDto.getContactCycle();
				this.phoneNumber = friendProfileRequestDto.getPhoneNumber();
				this.recentContactDate = friendProfileRequestDto.getRecentContactDate();
		}
}
