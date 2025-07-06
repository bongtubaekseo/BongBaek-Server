package org.appjam.bongbaek.domain.user.entity;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	@Id
	@Column(name = "user_id", columnDefinition = "BINARY(16)")
	private UUID memberId;

	@Column(name = "member_name", length = 30, nullable = false)
	private String memberName;

	@Column(name = "member_birthday", nullable = false)
	private LocalDate memberBirthday;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_income", columnDefinition = "VARCHAR(50)", nullable = false)
	private IncomeType memberIncome;

	@Column(name = "apple_id", updatable = false)
	private String appleId;

	@Column(name = "kakao_id", updatable = false)
	private Long kakaoId;

	@Builder
	private User(String username, LocalDate userBirthday, IncomeType userIncome, String appleId, Long kakaoId) {
		this.userId = UUID.randomUUID();
		this.username = username;
		this.userBirthday = userBirthday;
		this.userIncome = userIncome;
		this.appleId = appleId;
		this.kakaoId = kakaoId;
	}
}
