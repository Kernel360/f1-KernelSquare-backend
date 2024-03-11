package com.kernelsquare.domainmysql.domain.member_authority.entity;

import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "MemberAuthority")
@Table(name = "member_authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authority_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Authority authority;

	@Builder
	public MemberAuthority(Member member, Authority authority) {
		this.member = member;
		this.authority = authority;
	}

	public static MemberAuthority of(Member member, Authority authority) {
		return MemberAuthority.builder()
			.member(member)
			.authority(authority)
			.build();
	}
}
