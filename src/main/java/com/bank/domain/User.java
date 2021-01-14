package com.bank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity(name = "user")
public class User extends TimeUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // id
    @Column(columnDefinition = "varchar(255)")
    private String name; // 유저 이름

}
