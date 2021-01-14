package com.bank.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
    @Query(
            nativeQuery = true,
            value = "select * from point_history where USER_ID = :userId order by id desc limit 1"
    )
    PointHistory findByUserId(int userId);
    PointHistory findById(int id);
}
