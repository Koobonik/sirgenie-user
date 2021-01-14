package com.bank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "pointHistory")
public class PointHistory extends TimeUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private int inputPoint = 0;
    private int outputPoint = 0;
    @Column(columnDefinition = "varchar(20)")
    private String type; // input or output
    private int userPoint;
}
