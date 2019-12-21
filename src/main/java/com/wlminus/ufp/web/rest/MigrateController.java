//package com.wlminus.ufp.web.rest;
//
//import com.wlminus.ufp.domain.CourseSlotStatus;
//import com.wlminus.ufp.domain.StudentRegisterStatus;
//import com.wlminus.ufp.oracledomain.Course;
//import com.wlminus.ufp.oracledomain.Student;
//import com.wlminus.ufp.oraclerepository.CourseRepository;
//import com.wlminus.ufp.oraclerepository.StudentRepository;
//import com.wlminus.ufp.repository.CourseSlotStatusRepository;
//import com.wlminus.ufp.repository.StudentRegisterStatusRepository;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@RestController()
//@RequestMapping("/migrate")
//public class MigrateController {
//    private StudentRepository studentRepository;
//    private StudentRegisterStatusRepository studentRegisterStatusRepository;
//
//    private CourseRepository courseRepository;
//    private CourseSlotStatusRepository courseSlotStatusRepository;
//
//
//    public MigrateController(StudentRepository studentRepository, StudentRegisterStatusRepository studentRegisterStatusRepository, CourseRepository courseRepository, CourseSlotStatusRepository courseSlotStatusRepository) {
//        this.studentRepository = studentRepository;
//        this.studentRegisterStatusRepository = studentRegisterStatusRepository;
//        this.courseRepository = courseRepository;
//        this.courseSlotStatusRepository = courseSlotStatusRepository;
//    }
//
//    @GetMapping("/student")
//    public String migrateStudentCassandra() {
//        List<Student> dataOracle = studentRepository.findAll();
//        Set<StudentRegisterStatus> dataToCassandra = new HashSet<>();
//        for (Student stu: dataOracle) {
//            StudentRegisterStatus oneDataToCass = new StudentRegisterStatus();
//            oneDataToCass.setStudentId(stu.getStudentCode());
//            oneDataToCass.setMaxRegister(24L);
//            oneDataToCass.setIsPrior(1L);
//            oneDataToCass.setCurrentRegister(0L);
//            oneDataToCass.setId(UUID.randomUUID());
//
//            dataToCassandra.add(oneDataToCass);
//        }
//
//        studentRegisterStatusRepository.saveAll(dataToCassandra);
//
//        return "Migrate student done";
//    }
//
//    @GetMapping("/class")
//    public String migrateClassCassandra() {
//        List<Course> dataOracle = courseRepository.findAll();
//        Set<CourseSlotStatus> dataToCassandra = new HashSet<>();
//
//        for (Course cour: dataOracle) {
//            CourseSlotStatus oneDataToCass = new CourseSlotStatus();
//            oneDataToCass.setCourseCode(cour.getCourseCode());
//            oneDataToCass.setCurrentSlot(0L);
//            oneDataToCass.setMaxSlot(cour.getMaxSlot());
//            oneDataToCass.setId(UUID.randomUUID());
//
//            dataToCassandra.add(oneDataToCass);
//        }
//
//        courseSlotStatusRepository.saveAll(dataToCassandra);
//
//        return "Migrate class done";
//    }
//}
