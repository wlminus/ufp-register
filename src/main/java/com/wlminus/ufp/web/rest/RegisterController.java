package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.CourseSlotStatus;
import com.wlminus.ufp.domain.RequirePair;
import com.wlminus.ufp.domain.StudentRegisterStatus;
import com.wlminus.ufp.oracledomain.Course;
import com.wlminus.ufp.oracledomain.RegisterModel;
import com.wlminus.ufp.oracledomain.Schedule;
import com.wlminus.ufp.oracledomain.Student;
import com.wlminus.ufp.oraclerepository.CourseRepository;
import com.wlminus.ufp.oraclerepository.RegisterModelRepository;
import com.wlminus.ufp.oraclerepository.StudentRepository;
import com.wlminus.ufp.repository.CourseSlotStatusRepository;
import com.wlminus.ufp.repository.RequirePairRepository;
import com.wlminus.ufp.repository.StudentRegisterStatusRepository;
import com.wlminus.ufp.web.rest.vm.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RegisterController {
    private StudentRegisterStatusRepository studentRegisterStatusRepository;
    private CourseSlotStatusRepository courseSlotStatusRepository;
    private CourseRepository courseRepository;
    private RequirePairRepository requirePairRepository;
    private RegisterModelRepository registerModelRepository;
    private StudentRepository studentRepository;

    public RegisterController(StudentRegisterStatusRepository studentRegisterStatusRepository, CourseSlotStatusRepository courseSlotStatusRepository, CourseRepository courseRepository, RequirePairRepository requirePairRepository, RegisterModelRepository registerModelRepository, StudentRepository studentRepository) {
        this.studentRegisterStatusRepository = studentRegisterStatusRepository;
        this.courseSlotStatusRepository = courseSlotStatusRepository;
        this.courseRepository = courseRepository;
        this.requirePairRepository = requirePairRepository;
        this.registerModelRepository = registerModelRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> registerClass(@RequestBody RegisterRequest request) {
        System.out.println(request.getStudentCode());
        System.out.println(request.getRequestCourse());

        String studentCode = request.getStudentCode();
        List<String> requestCourse = request.getRequestCourse();

        // Check student first
        Optional<StudentRegisterStatus> dataStudentStatus = studentRegisterStatusRepository.findByStudentId(studentCode);
        if (dataStudentStatus.isPresent()) {
            // Check if request course duplicates
            Set<String> setCourse = new HashSet<>(requestCourse);
            if (setCourse.size() < requestCourse.size()) {
                return ResponseEntity.badRequest().body("Duplicates course");
            }

            // Check if request course have duplicates subject
            // Check course timetable
            List<Course> listCourseChoose = courseRepository.findAllByCourseCodeInList(requestCourse);
            for (int i = 0; i < listCourseChoose.size(); i++) {
                for (int j = 0; j < listCourseChoose.size(); j++) {
                    if (i != j) {
                        if (listCourseChoose.get(i).getSubject().getSubjectCode().equals(listCourseChoose.get(j).getSubject().getSubjectCode())) {
                            return ResponseEntity.badRequest().body("Duplicates subject");
                        }
                        if (isSubjectViolent(listCourseChoose.get(i), listCourseChoose.get(j))) {
                            return ResponseEntity.badRequest().body("Timetable violent");
                        }
                    }
                }
            }

            // Check if student request status not valid
            StudentRegisterStatus realStudentStatus = dataStudentStatus.get();
            if (realStudentStatus.getIsPrior() != 1) {
                return ResponseEntity.badRequest().body("Student is not allow to register class this time");
            }
            Long creditValueRequested = courseRepository.sumCreditValueByCourseIn(requestCourse);
            if ((realStudentStatus.getCurrentRegister() + creditValueRequested) > realStudentStatus.getMaxRegister()) {
                return ResponseEntity.badRequest().body("Max register reach");
            }

            // Check course pair
            List<String> lstSubjectRequest = new ArrayList<>();
            for (Course course : listCourseChoose) {
                lstSubjectRequest.add(course.getSubject().getSubjectCode());
            }
            for (Course course : listCourseChoose) {
                Optional<RequirePair> requirePair = requirePairRepository.findBySubjectCode(course.getSubject().getSubjectCode());
                if (requirePair.isPresent()) {
                    String require = requirePair.get().getRequireCode();
                    if (!lstSubjectRequest.contains(require)) {
                        return ResponseEntity.badRequest().body("Course " + requirePair.get().getSubjectCode() + " require " + require);
                    }
                }
            }

            // Check slot
            for (String str : requestCourse) {
                Optional<CourseSlotStatus> slotStatuses = courseSlotStatusRepository.findByCourseCode(str);
                if (slotStatuses.isPresent()) {
                    if (slotStatuses.get().getCurrentSlot() + 1 > slotStatuses.get().getMaxSlot()) {
                        return ResponseEntity.badRequest().body("Course " + slotStatuses.get().getCourseCode() + " reach max register");
                    }
                }
            }

            // All check done register process
            Set<RegisterModel> newRegisterList = new HashSet<>();
            Student stu = studentRepository.findByStudentCode(studentCode);
            // Update student register status
            Long current = realStudentStatus.getCurrentRegister();
            realStudentStatus.setCurrentRegister(current + creditValueRequested);
            studentRegisterStatusRepository.save(realStudentStatus);
            // Update course slot status
            for (Course cour : listCourseChoose) {
                Optional<CourseSlotStatus> slotStatuses = courseSlotStatusRepository.findByCourseCode(cour.getCourseCode());
                CourseSlotStatus courseSlotStatus = slotStatuses.get();
                courseSlotStatus.setCurrentSlot(courseSlotStatus.getCurrentSlot() + 1);
                courseSlotStatusRepository.save(courseSlotStatus);

                cour.setCurrentSlot(cour.getCurrentSlot() + 1);
                courseRepository.save(cour);

                RegisterModel tmp = new RegisterModel();

                tmp.setCourse(cour);
                tmp.setSemester("2020A");
                tmp.setStudent(stu);
                tmp.setStatus("active");

                newRegisterList.add(tmp);
            }

            registerModelRepository.saveAll(newRegisterList);
            return ResponseEntity.ok().body("Register done");
        } else {
            return ResponseEntity.badRequest().body("Student not existed");
        }
    }

    @PostMapping("/remove")
    @Transactional
    public ResponseEntity<String> deleteClass(@RequestBody RegisterRequest request) {
        try {
            //Remove class process
            String studentCode = request.getStudentCode();
            List<String> requestCourse = request.getRequestCourse();

            List<Course> listCourseChooseToRemove = courseRepository.findAllByCourseCodeInList(requestCourse);


            Optional<StudentRegisterStatus> dataStudentStatus = studentRegisterStatusRepository.findByStudentId(studentCode);
            if (dataStudentStatus.isPresent()) {
                StudentRegisterStatus realStudentStatus = dataStudentStatus.get();

                // Check if request course duplicates
                Set<String> setCourse = new HashSet<>(requestCourse);
                if (setCourse.size() < requestCourse.size()) {
                    return ResponseEntity.badRequest().body("Duplicates course");
                }
                //Update student request status
                Long creditValueRequested = courseRepository.sumCreditValueByCourseIn(requestCourse);
                Long current = realStudentStatus.getCurrentRegister();
                realStudentStatus.setCurrentRegister(current - creditValueRequested);
                studentRegisterStatusRepository.save(realStudentStatus);

                Student stu = studentRepository.findByStudentCode(studentCode);

                // Update course slot status
                for (Course cour : listCourseChooseToRemove) {
                    Optional<CourseSlotStatus> slotStatuses = courseSlotStatusRepository.findByCourseCode(cour.getCourseCode());
                    CourseSlotStatus courseSlotStatus = slotStatuses.get();
                    courseSlotStatus.setCurrentSlot(courseSlotStatus.getCurrentSlot() - 1);
                    courseSlotStatusRepository.save(courseSlotStatus);

                    cour.setCurrentSlot(cour.getCurrentSlot() - 1);
                    courseRepository.save(cour);

                    registerModelRepository.deleteByStudentIdAndCourseId(stu.getId(), cour.getId());
                }

                return ResponseEntity.ok().body("Remove done");
            } else {
                return ResponseEntity.badRequest().body("Student not existed");
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Err" + ex.getMessage());
        }
    }

    private boolean isScheduleViolent(Schedule first, Schedule second) {
        List<String> weekList1 = Arrays.asList(first.getWeekValue().split(","));
        List<String> weekList2 = Arrays.asList(second.getWeekDayValue().split(","));
        if (!Collections.disjoint(weekList1, weekList2)) {
            List<String> weekDayList1 = Arrays.asList(first.getWeekDayValue().split(","));
            List<String> weekDayList2 = Arrays.asList(second.getWeekDayValue().split(","));
            if (!Collections.disjoint(weekDayList1, weekDayList2)) {
                List<String> periodList1 = Arrays.asList(first.getPeriodValue().split(","));
                List<String> periodList2 = Arrays.asList(second.getPeriodValue().split(","));
                return !Collections.disjoint(periodList1, periodList2);
            }
        }
        return false;
    }

    private boolean isSubjectViolent(Course first, Course second) {
        Set<Schedule> firstSchedule = first.getSchedules();
        Set<Schedule> secondSchedule = second.getSchedules();
        for (Schedule sc : firstSchedule) {
            for (Schedule sc2 : secondSchedule) {
                if (isScheduleViolent(sc, sc2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
