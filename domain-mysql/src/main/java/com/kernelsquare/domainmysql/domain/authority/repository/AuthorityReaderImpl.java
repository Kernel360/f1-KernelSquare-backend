package com.kernelsquare.domainmysql.domain.authority.repository;

import com.kernelsquare.core.common_response.error.code.AuthorityErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityReaderImpl implements AuthorityReader {
    private final AuthorityRepository authorityRepository;

    @Override
    public Authority findAuthority(AuthorityType authorityType) {
        return authorityRepository.findAuthorityByAuthorityType(authorityType)
            .orElseThrow(() -> new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND));
    }
}
