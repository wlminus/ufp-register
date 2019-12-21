package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.StudentRegisterStatus;
import com.wlminus.ufp.oracledomain.Course;
import com.wlminus.ufp.oraclerepository.CourseRepository;
import com.wlminus.ufp.repository.CourseSlotStatusRepository;
import com.wlminus.ufp.repository.StudentRegisterStatusRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RegisterController {
    private StudentRegisterStatusRepository studentRegisterStatusRepository;
    private CourseSlotStatusRepository courseSlotStatusRepository;
    private CourseRepository courseRepository;

    public RegisterController(StudentRegisterStatusRepository studentRegisterStatusRepository, CourseSlotStatusRepository courseSlotStatusRepository, CourseRepository courseRepository) {
        this.studentRegisterStatusRepository = studentRegisterStatusRepository;
        this.courseSlotStatusRepository = courseSlotStatusRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerClass(@RequestBody List<String> requestCourse, @RequestBody String studentCode) {
        List<Course> listCourseChoose = courseRepository.findAllByCourseCodeIn(requestCourse);
//        Long totalRequestCredit =

        // Check student
        Optional<StudentRegisterStatus> dataStudentStatus = studentRegisterStatusRepository.findByStudentId(studentCode);
        if (dataStudentStatus.isPresent()) {
            StudentRegisterStatus realStudentStatus = dataStudentStatus.get();
            if (realStudentStatus.getIsPrior() != 1) {
                return ResponseEntity.badRequest().body("Student is not allow to register class this time");
            }


        }

        return ResponseEntity.ok("register done");
    }
}
