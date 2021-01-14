package com.bank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;

@Getter
@Setter
public class TimeUtil {
    @Column(columnDefinition = "datetime")
    private Timestamp createdAt; // 가입 날짜
    @Column(columnDefinition = "datetime")
    private Timestamp updatedAt; // 업데이트 날짜
}
