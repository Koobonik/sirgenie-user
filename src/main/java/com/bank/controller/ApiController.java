package com.bank.controller;

import com.bank.service.FolderService;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ApiController {
    private final FolderService folderService;

    @GetMapping("createFolder")
    public ResponseEntity<?> createFolder(@RequestParam int userId,
                                         @RequestParam String folderName) throws IOException {
        return folderService.createFolder(userId, folderName);
    }
    /*
    * 자신의 특정 폴더에 업로드된 사진을 저장할 수 있다.
    * 1. URL이 저장된다고 가정
    * 2. N개의 사진을 동시에 저장 가능 (List로 받으면 될듯)
     */

    /*
    * 1. 생성순서대로 폴더 조회 (폴더에 created_at 넣어주자)
    */

    /*
     * 1. 특정 폴더에 최근 저장한 순서대로 사진을 조회할 수 있다. (사진마다 index를 받아야 함)
     */
}
