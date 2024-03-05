package com.kernelsquare.domainmysql.domain.social_login.repository;

import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.domainmysql.domain.social_login.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {

    Boolean existsByEmailAndSocialProvider(String email, SocialProvider socialProvider);
}
