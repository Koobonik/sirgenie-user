package com.bank.dto;

import com.bank.domain.Picture;
import lombok.Data;

import java.util.List;

@Data
public class PictureRequestDto {
    private int index;
    private String url;
    private String folderName; // 폴더의 경로이자 이름
    private int userId;
    private List<String> tag;

    public Picture toEntity(){
        return new Picture().builder()
                .index(this.index)
                .url(this.url)
                .folderName(this.folderName)
                .userId(this.userId)
                .tag(this.tag)
                .build();
    }
}
