package com.kernelsquare.domainmysql.domain.rank.repository;

import com.kernelsquare.domainmysql.domain.rank.entity.Rank;

import java.util.Optional;

public interface RankReader {
    Rank findByName(Long name);
}
