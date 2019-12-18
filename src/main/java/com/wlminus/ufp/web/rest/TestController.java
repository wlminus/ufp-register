package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.User;
import com.wlminus.ufp.oraclerepository.SubjectRepository;
import com.wlminus.ufp.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private SubjectRepository subjectRepository;
    private UserRepository userRepository;

    public TestController(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/subject")
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
