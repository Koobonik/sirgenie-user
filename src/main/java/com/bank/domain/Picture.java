package com.bank.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "picture")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int index;
    private String url;
    private String folderName; // 폴더의 경로이자 이름
    private int userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> tag = new ArrayList<>();
}
