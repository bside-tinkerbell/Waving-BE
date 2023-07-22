package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.ContactEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ContactRequestDto {

		// 지인
		private Long contactId;                                 // 연락처 id (friend 테이블)
		private Long userId;                                    // 회원 id
		@Min(0)
		@Max(1)
		private int linkType;                                   // 연동 유형 (0:기기, 1:카카오)

		private List<FriendProfileRequestDto> profileList = new ArrayList<>();   // 프로필 DTO list


		// DTO -> Entity
		public ContactEntity toFriendEntity() {
				return ContactEntity.builder()
								.contactId(contactId)
								.userId(userId)
								.linkType(linkType)
								.build();
		}

		@Builder
		public ContactRequestDto(Long contactId, Long userId, int linkType, List<FriendProfileRequestDto> profileList) {
				this.contactId = contactId;
				this.userId = userId;
				this.linkType = linkType;
				this.profileList = profileList;
		}


}
