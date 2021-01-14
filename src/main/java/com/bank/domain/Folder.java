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
    @Column(columnDefinition = "varchar(50)", unique = true)
    private String folderName; // 폴더의 경로이자 이름
}
