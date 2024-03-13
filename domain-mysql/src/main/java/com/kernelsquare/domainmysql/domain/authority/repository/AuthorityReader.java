package com.kernelsquare.domainmysql.domain.authority.repository;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;

public interface AuthorityReader {
    Authority findAuthority(AuthorityType authorityType);
}
