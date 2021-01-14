package com.bank.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Integer> {
    @Query(
            nativeQuery = true,
            value = "select * from folder where user_id = :userId and folder_point > 0"
    )
    List<Folder> findAllByUserId(int userId);

    Folder findByUserIdAndFolderName(int userId, String folderName);
}
