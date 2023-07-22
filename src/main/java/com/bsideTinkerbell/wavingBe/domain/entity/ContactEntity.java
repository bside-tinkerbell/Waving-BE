package com.bsideTinkerbell.wavingBe.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@Table(catalog = "friend", name = "contact")
public class ContactEntity {
		// 연락처 연동 유형 table

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long contactId;     // 연락처 고유id
		@Column(nullable = false)
		private Long userId;       // 회원id
		@ColumnDefault("0")
		private int linkType;  // 연동유형 (0:기기, 1:카카오)

		@Builder
		public ContactEntity(Long contactId, Long userId, int linkType) {
				this.contactId = contactId;
				this.userId = userId;
				this.linkType = linkType;
		}

}

