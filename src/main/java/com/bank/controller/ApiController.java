package com.bank.controller;

import com.bank.dto.PictureRequestDto;
import com.bank.service.FolderService;
import com.bank.service.PictureService;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiController {
    private final FolderService folderService;
    private final PictureService pictureService;
    @GetMapping("createFolder")
    public ResponseEntity<?> createFolder(@RequestParam int userId,
                                         @RequestParam String folderName) {
        return folderService.createFolder(userId, folderName);
    }

    // 해당 코드도 마찬가지로 유저 검증은 생략
    @PostMapping("uploadPictures")
    public ResponseEntity<?> uploadPictures(@RequestParam int userId, @RequestBody List<PictureRequestDto> pictureRequestDto){
        // dto에 태그 추가해주자
        return pictureService.savePictures(userId, pictureRequestDto);
    }

    @GetMapping("readTop10")
    public ResponseEntity<?> readTop10(){
        return pictureService.readTop10Tag();
    }



    /*
    * 1. 생성순서대로 폴더 조회 (폴더에 created_at 넣어주자)
    */

    /*
     * 1. 특정 폴더에 최근 저장한 순서대로 사진을 조회할 수 있다. (사진마다 index를 받아야 함)
     */
}
