package com.kernelsquare.domainmysql.domain.rank.repository;

import com.kernelsquare.core.common_response.error.code.RankErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankReaderImpl implements RankReader {
    public final RankRepository rankRepository;

    @Override
    public Rank findRank(Long name) {
        return rankRepository.findByName(name)
            .orElseThrow(() -> new BusinessException(RankErrorCode.RANK_NOT_FOUND));
    }
}
