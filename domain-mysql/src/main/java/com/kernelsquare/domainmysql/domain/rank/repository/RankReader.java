package com.kernelsquare.domainmysql.domain.rank.repository;

import com.kernelsquare.domainmysql.domain.rank.entity.Rank;

public interface RankReader {
    Rank findRank(Long name);
}
