package edu.sda26.springcourse.service;

import edu.sda26.springcourse.model.MyUser;
import edu.sda26.springcourse.repository.MyUserRepository;
import org.springframework.stereotype.Service;

@Service
public class MyUserService {

 private final MyUserRepository myUserRepository;

    public MyUserService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    public void save(MyUser myUser) {
        myUserRepository.save(myUser);
    }


}
