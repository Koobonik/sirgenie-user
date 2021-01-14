package com.bank.service;

import com.bank.domain.*;
import com.bank.dto.PictureRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepository pictureRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final FolderRepository folderRepository;

    public List<Picture> saveAll(List<Picture> pictureList){
        return pictureRepository.saveAll(pictureList);
    }

    /*
     * 자신의 특정 폴더에 업로드된 사진을 저장할 수 있다.
     * 1. URL이 저장된다고 가정
     * 2. N개의 사진을 동시에 저장 가능 (List로 받으면 될듯)
     */

    // 폴더에서 먼저 돈을 깍아주고 마지막에 포인트 히스토리에서 없애준다.
    public ResponseEntity<?> savePictures(int userId, List<PictureRequestDto> pictureRequestDtoList){
        List<Folder> folder = folderRepository.findAllByUserId(userId);
        // 우선 사이즈가 있고 해당 사이즈 만큼 유저의 폴더에 귀속되어 있는 포인트가 여유로운지 검증해야 함. 아니면 뱉어야 함
        int point = 0;
        for(Folder h : folder){
            point += h.getFolderPoint();
        }
        int usePoint = pictureRequestDtoList.size() * 100; // 사용해야할 포인트
        if(point < usePoint){
            // 안될경우
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        // 10장올려야하는데 1번 폴더에 900 있고 2번 폴더에 1000 있다고 가정
        for(Folder h : folder){
            //           1000   -   900 밖에없어서 ㅠㅠ 남을거야 이후에 처리해줘야해
            usePoint -= h.getFolderPoint(); // 900 - 1000
            if(usePoint > 0){
                //남았네 ㅠㅠ 한번 더 돌려야해~
                h.setFolderPoint(0);
                folderRepository.save(h);
            }
            else if(usePoint <= 0) {
                // 다 돌아감!
                usePoint *= -1;
                h.setFolderPoint(usePoint);
                folderRepository.save(h);
                break;
            }
        }


        List<Picture> pictureList = new ArrayList<>();
        for(PictureRequestDto picture : pictureRequestDtoList){
            pictureList.add(picture.toEntity());
        }
        saveAll(pictureList);
        return new ResponseEntity<>(pictureList, HttpStatus.OK);
    }

}
