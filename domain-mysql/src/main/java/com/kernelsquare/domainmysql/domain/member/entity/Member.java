package com.kernelsquare.domainmysql.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Member")
@Table(name = "member")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "nickname", columnDefinition = "varchar(20)")
	private String nickname;

	@Column(nullable = false, unique = true, name = "email", columnDefinition = "varchar(40)")
	private String email;

	@Column(nullable = false, name = "password", columnDefinition = "varchar(255)")
	private String password;

	@Column(nullable = false, name = "experience", columnDefinition = "bigint")
	private Long experience;

	@Column(name = "image_url", columnDefinition = "varchar(1000)")
	private String imageUrl;

	@Column(name = "introduction", columnDefinition = "text")
	private String introduction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Level level;

	@OneToMany(mappedBy = "member")
	private List<MemberAuthority> authorities = new ArrayList<>();

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void updateImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateLevel(Level level) {
		this.level = level;
	}

	public Long getExperience() {
		return this.experience;
	}

	public void updateExperience(Long experience) {
		this.experience = experience;
	}

	public void addExperience(Long experience) {
		this.experience += experience;
	}

	public boolean isExperienceExceed(Long experience) {
		if (experience >= this.level.getLevelUpperLimit()) {
			return true;
		}
		return false;
	}

	public void initAuthorities(List<MemberAuthority> authorities) {
		this.authorities = authorities;
	}

	@Builder
	public Member(Long id, String nickname, String email, String password, Long experience, String imageUrl,
		String introduction, Level level, List<MemberAuthority> authorities) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.experience = experience;
		this.imageUrl = imageUrl;
		this.introduction = introduction;
		this.level = level;
		this.authorities = new ArrayList<>();
	}
}