package com.bank.service;

import com.bank.domain.User;
import com.bank.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public int save(User user){
        return userRepository.save(user).getId();
    }
    @Bean
    public void insertUser(){
        User user = new User();
        user.setId(0);
        user.setName("dev_koo");
        user.setCreatedAt(new Timestamp(new Date().getTime()));
        user.setUpdatedAt(new Timestamp(new Date().getTime()));
        save(user);
    }
}
