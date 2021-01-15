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
import java.util.*;

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
            return new ResponseEntity<>("포인트가 부족합니다.",HttpStatus.CONFLICT);
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

        int minus = pictureRequestDtoList.size() * 100;
        PointHistory pointHistory2 = pointHistoryRepository.findByUserId(userId);
        PointHistory pointHistory = new PointHistory();
        pointHistory.setType("사진 업로드");
        pointHistory.setOutputPoint(minus);
        pointHistory.setUserPoint(pointHistory2.getUserPoint() - minus);
        pointHistory.setUserId(userId);
        pointHistoryRepository.save(pointHistory);
        // 폴더 이름 검증도 해야함
        List<Picture> pictureList = new ArrayList<>();
        for(PictureRequestDto picture : pictureRequestDtoList){
            if(folderRepository.findByUserIdAndFolderName(userId, picture.getFolderName()) == null){
                return new ResponseEntity<>("존재하지 않는 폴더에 사진을 올릴 수 없습니다.", HttpStatus.CONFLICT);
            }
            pictureList.add(picture.toEntity());

        }
        saveAll(pictureList);
        return new ResponseEntity<>(pictureList, HttpStatus.OK);
    }

    // 유저 각 개인마다의 통계인지 전체 사진마다의 통계인지 명확하게 명시되어 있지 않기에 전체로 판단하여 추출
    // -> 통계라 함은 수많은 데이터 셋에서 무언가의 가치를 찾는 것이기에 전체 사진에서 흐름을 찾을 수 있는 것이 좋다고 생각합니다.
    // 개인이라 하더라도 구현에 전혀 문제 없음 소스코드 한줄 바꾸어주면 됨. findAll에서 유저 아이디에 맞게만 값 찾아내면 끝.
    public ResponseEntity<?> readTop10Tag(){
        List<Picture> pictureList = pictureRepository.findAll();
        Map<String, Integer> h = new HashMap<>();
        for(Picture picture : pictureList){
            for(String str: picture.getTag()){
                if(h.get(str) == null){
                    // 처음에 값이 없을 경우
                    h.put(str, 1);
                }
                else {
                    // 값이 있을경우
                    h.put(str, h.get(str) + 1);
                }
                // h.merge(str, 1, Integer::sum); // 위에 if문은 해당 라인으로 대체 가능함
            }
        }
        ArrayList keySetList = new ArrayList(h.keySet());
        Collections.sort(keySetList, (o1, o2) -> (h.get(o2).compareTo(h.get(o1))));
        Map<String, Integer> hi = new HashMap<>();
        int count = 0;
        for(Object key : keySetList) {
            System.out.println("key : " + key + " / " + "value : " + h.get(key));
            hi.put(key.toString(), h.get(key));
            if(count == 9){
                return new ResponseEntity<>(hi, HttpStatus.OK);
            }
            count++;
        }
        return new ResponseEntity<>(hi, HttpStatus.OK);
    }

}
