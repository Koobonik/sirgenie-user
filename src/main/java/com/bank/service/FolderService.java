package com.bank.service;

import com.bank.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;


    // 폴더생성
    // 1조건 : 생성시 폴더에 대한 이름을 정할 수 있음
    // 2조건 : 폴더의 생성 수는 제한이 없다.
    // 3조건 : 폴더 생성시 유저는 1,000포인트 획득
    // 4조건 : 업로드 시 100포인트 소모
    // 5조건 : 포인트가 모자랄 시 업로드 불가 (400포인트 있는데 5장 올리면 아이에 막아버리자 -> 다시 선택할 기회를 준다는 뜻으로)
    // 6조건 : 선입 선출을 추적해야하므로 포인트 히스토리가 있으면 좋음
    // 추가적으로 요구되는 것 : 폴더 테이블, 포인트 히스토리 테이블 tpye -> insert, use
    // 조건을 읽어보니 획득한 먼저 획득한 포인트를 먼저 사용하게끔 만드는 것이 목적인것으로 판단.

    // 실제로는 유저의 정보를 담고 있는 세션이라던가 jwt를 받아오는 것이 검증을 위해 맞지만 간단한 코딩테스트 이므로
    // 유저 정보를 받아온다고 가정하며 그 과정은 생략하도록 하겠습니다.
    @Transactional
    public ResponseEntity<?> createFolder(int userId, String folderName){
        Folder folder = new Folder();
        folder.setUserId(userId);
        folder.setFolderName(folderName);
        folder.setFolderPoint(1000);
        Folder folder1;
        try {
            // 가장 최근에 있었던 포인트 입출금 내역
            PointHistory pointHistory = pointHistoryRepository.findByUserId(userId);
            if(pointHistory == null){
                // 만약에 없다면 하나 만들어주자!
                pointHistory = new PointHistory();
                pointHistory.setType("폴더생성");
                pointHistory.setInputPoint(1000);
                pointHistory.setUserPoint(pointHistory.getUserPoint() + pointHistory.getInputPoint());
                pointHistory.setUserId(userId);
                pointHistoryRepository.save(pointHistory);
            }
            else {
                // 있을 경우
                PointHistory pointHistory1 = new PointHistory();
                pointHistory1.setType("폴더생성");
                pointHistory1.setInputPoint(1000);
                pointHistory1.setUserPoint(pointHistory.getUserPoint() + pointHistory1.getInputPoint());
                pointHistory.setUserId(userId);
                pointHistoryRepository.save(pointHistory1);
            }

            if(folderRepository.findByUserIdAndFolderName(userId, folderName) == null){
                folder1 = folderRepository.save(folder);
                return new ResponseEntity<>(folder1, HttpStatus.OK);
            }
        } catch (Exception e){
//            throw new IOException("저장중 에러가 발생했습니다.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
