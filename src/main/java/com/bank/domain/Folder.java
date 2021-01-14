package com.bank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Folder extends TimeUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    @Column(columnDefinition = "varchar(50)")
    private String folderName; // 폴더의 경로이자 이름
    private int folderPoint; // 폴더에 귀속되어있는 포인트 -> 이후에 선입 선출할 때 쓰일 예정
}
