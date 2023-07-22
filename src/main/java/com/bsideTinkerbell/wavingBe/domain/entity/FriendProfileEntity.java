package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "friend", name = "friend_profile")
public class FriendProfileEntity {
		// 연락처 프로필 table

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long friendProfileId;                           // 연락처 프로필 고유id
		@Column(nullable = false)
		private Long contactId;                                 // 연락처 id (friend 테이블)
		private int isFavorite;                                 // 즐겨찾기
		@Column(nullable = false)
		private String name;                                    // 지인 이름
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate birthday;                             // 지인 생일
		@ColumnDefault("4")
		private int contactCycle = 4;                               // 지인 연락 주기
		@Column(nullable = false)
		private String phoneNumber;                             // 지인 연락처
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private LocalDate recentContactDate;                    // 최근연락일(마지막연락일)
		@CreatedDate
		@Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
		private LocalDateTime createdAt;                         // 생성일
		@LastModifiedDate
		@Column(columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
		private LocalDateTime updatedAt;                         // 수정일

		private LocalDateTime deletedAt = null;                  // 삭제일


		@Builder
		public FriendProfileEntity(Long friendProfileId, Long contactId, int isFavorite, String name, LocalDate birthday, int contactCycle, String phoneNumber, LocalDate recentContactDate) {
				this.friendProfileId = friendProfileId;
				this.contactId = contactId;
				this.isFavorite = isFavorite;
				this.name = name;
				this.birthday = birthday;
				this.contactCycle = contactCycle;
				this.phoneNumber = phoneNumber;
				this.recentContactDate = recentContactDate;
		}

		public void delete(){
				this.deletedAt = LocalDateTime.now();
		}


}
