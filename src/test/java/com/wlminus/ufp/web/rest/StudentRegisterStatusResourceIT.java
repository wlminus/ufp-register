package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.AbstractCassandraTest;
import com.wlminus.ufp.RegisterApp;
import com.wlminus.ufp.domain.StudentRegisterStatus;
import com.wlminus.ufp.repository.StudentRegisterStatusRepository;
import com.wlminus.ufp.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.UUID;

import static com.wlminus.ufp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudentRegisterStatusResource} REST controller.
 */
@SpringBootTest(classes = RegisterApp.class)
public class StudentRegisterStatusResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_IS_PRIOR = 1L;
    private static final Long UPDATED_IS_PRIOR = 2L;

    private static final Long DEFAULT_MAX_REGISTER = 1L;
    private static final Long UPDATED_MAX_REGISTER = 2L;

    private static final Long DEFAULT_CURRENT_REGISTER = 1L;
    private static final Long UPDATED_CURRENT_REGISTER = 2L;

    @Autowired
    private StudentRegisterStatusRepository studentRegisterStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restStudentRegisterStatusMockMvc;

    private StudentRegisterStatus studentRegisterStatus;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRegisterStatus createEntity() {
        StudentRegisterStatus studentRegisterStatus = new StudentRegisterStatus()
            .studentId(DEFAULT_STUDENT_ID)
            .isPrior(DEFAULT_IS_PRIOR)
            .maxRegister(DEFAULT_MAX_REGISTER)
            .currentRegister(DEFAULT_CURRENT_REGISTER);
        return studentRegisterStatus;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRegisterStatus createUpdatedEntity() {
        StudentRegisterStatus studentRegisterStatus = new StudentRegisterStatus()
            .studentId(UPDATED_STUDENT_ID)
            .isPrior(UPDATED_IS_PRIOR)
            .maxRegister(UPDATED_MAX_REGISTER)
            .currentRegister(UPDATED_CURRENT_REGISTER);
        return studentRegisterStatus;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentRegisterStatusResource studentRegisterStatusResource = new StudentRegisterStatusResource(studentRegisterStatusRepository);
        this.restStudentRegisterStatusMockMvc = MockMvcBuilders.standaloneSetup(studentRegisterStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        studentRegisterStatusRepository.deleteAll();
        studentRegisterStatus = createEntity();
    }

    @Test
    public void createStudentRegisterStatus() throws Exception {
        int databaseSizeBeforeCreate = studentRegisterStatusRepository.findAll().size();

        // Create the StudentRegisterStatus
        restStudentRegisterStatusMockMvc.perform(post("/api/student-register-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentRegisterStatus)))
            .andExpect(status().isCreated());

        // Validate the StudentRegisterStatus in the database
        List<StudentRegisterStatus> studentRegisterStatusList = studentRegisterStatusRepository.findAll();
        assertThat(studentRegisterStatusList).hasSize(databaseSizeBeforeCreate + 1);
        StudentRegisterStatus testStudentRegisterStatus = studentRegisterStatusList.get(studentRegisterStatusList.size() - 1);
        assertThat(testStudentRegisterStatus.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testStudentRegisterStatus.getIsPrior()).isEqualTo(DEFAULT_IS_PRIOR);
        assertThat(testStudentRegisterStatus.getMaxRegister()).isEqualTo(DEFAULT_MAX_REGISTER);
        assertThat(testStudentRegisterStatus.getCurrentRegister()).isEqualTo(DEFAULT_CURRENT_REGISTER);
    }

    @Test
    public void createStudentRegisterStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRegisterStatusRepository.findAll().size();

        // Create the StudentRegisterStatus with an existing ID
        studentRegisterStatus.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentRegisterStatusMockMvc.perform(post("/api/student-register-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentRegisterStatus)))
            .andExpect(status().isBadRequest());

        // Validate the StudentRegisterStatus in the database
        List<StudentRegisterStatus> studentRegisterStatusList = studentRegisterStatusRepository.findAll();
        assertThat(studentRegisterStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllStudentRegisterStatuses() throws Exception {
        // Initialize the database
        studentRegisterStatus.setId(UUID.randomUUID());
        studentRegisterStatusRepository.save(studentRegisterStatus);

        // Get all the studentRegisterStatusList
        restStudentRegisterStatusMockMvc.perform(get("/api/student-register-statuses"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentRegisterStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].isPrior").value(hasItem(DEFAULT_IS_PRIOR.intValue())))
            .andExpect(jsonPath("$.[*].maxRegister").value(hasItem(DEFAULT_MAX_REGISTER.intValue())))
            .andExpect(jsonPath("$.[*].currentRegister").value(hasItem(DEFAULT_CURRENT_REGISTER.intValue())));
    }

    @Test
    public void getStudentRegisterStatus() throws Exception {
        // Initialize the database
        studentRegisterStatus.setId(UUID.randomUUID());
        studentRegisterStatusRepository.save(studentRegisterStatus);

        // Get the studentRegisterStatus
        restStudentRegisterStatusMockMvc.perform(get("/api/student-register-statuses/{id}", studentRegisterStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(studentRegisterStatus.getId().toString()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.isPrior").value(DEFAULT_IS_PRIOR.intValue()))
            .andExpect(jsonPath("$.maxRegister").value(DEFAULT_MAX_REGISTER.intValue()))
            .andExpect(jsonPath("$.currentRegister").value(DEFAULT_CURRENT_REGISTER.intValue()));
    }

    @Test
    public void getNonExistingStudentRegisterStatus() throws Exception {
        // Get the studentRegisterStatus
        restStudentRegisterStatusMockMvc.perform(get("/api/student-register-statuses/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStudentRegisterStatus() throws Exception {
        // Initialize the database
        studentRegisterStatus.setId(UUID.randomUUID());
        studentRegisterStatusRepository.save(studentRegisterStatus);

        int databaseSizeBeforeUpdate = studentRegisterStatusRepository.findAll().size();

        // Update the studentRegisterStatus
        StudentRegisterStatus updatedStudentRegisterStatus = studentRegisterStatusRepository.findById(studentRegisterStatus.getId()).get();
        updatedStudentRegisterStatus
            .studentId(UPDATED_STUDENT_ID)
            .isPrior(UPDATED_IS_PRIOR)
            .maxRegister(UPDATED_MAX_REGISTER)
            .currentRegister(UPDATED_CURRENT_REGISTER);

        restStudentRegisterStatusMockMvc.perform(put("/api/student-register-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudentRegisterStatus)))
            .andExpect(status().isOk());

        // Validate the StudentRegisterStatus in the database
        List<StudentRegisterStatus> studentRegisterStatusList = studentRegisterStatusRepository.findAll();
        assertThat(studentRegisterStatusList).hasSize(databaseSizeBeforeUpdate);
        StudentRegisterStatus testStudentRegisterStatus = studentRegisterStatusList.get(studentRegisterStatusList.size() - 1);
        assertThat(testStudentRegisterStatus.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testStudentRegisterStatus.getIsPrior()).isEqualTo(UPDATED_IS_PRIOR);
        assertThat(testStudentRegisterStatus.getMaxRegister()).isEqualTo(UPDATED_MAX_REGISTER);
        assertThat(testStudentRegisterStatus.getCurrentRegister()).isEqualTo(UPDATED_CURRENT_REGISTER);
    }

    @Test
    public void updateNonExistingStudentRegisterStatus() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterStatusRepository.findAll().size();

        // Create the StudentRegisterStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRegisterStatusMockMvc.perform(put("/api/student-register-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentRegisterStatus)))
            .andExpect(status().isBadRequest());

        // Validate the StudentRegisterStatus in the database
        List<StudentRegisterStatus> studentRegisterStatusList = studentRegisterStatusRepository.findAll();
        assertThat(studentRegisterStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStudentRegisterStatus() throws Exception {
        // Initialize the database
        studentRegisterStatus.setId(UUID.randomUUID());
        studentRegisterStatusRepository.save(studentRegisterStatus);

        int databaseSizeBeforeDelete = studentRegisterStatusRepository.findAll().size();

        // Delete the studentRegisterStatus
        restStudentRegisterStatusMockMvc.perform(delete("/api/student-register-statuses/{id}", studentRegisterStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentRegisterStatus> studentRegisterStatusList = studentRegisterStatusRepository.findAll();
        assertThat(studentRegisterStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
