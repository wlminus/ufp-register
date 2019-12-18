package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.AbstractCassandraTest;
import com.wlminus.ufp.RegisterApp;
import com.wlminus.ufp.domain.CourseSlotStatus;
import com.wlminus.ufp.repository.CourseSlotStatusRepository;
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
 * Integration tests for the {@link CourseSlotStatusResource} REST controller.
 */
@SpringBootTest(classes = RegisterApp.class)
public class CourseSlotStatusResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_COURSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_MAX_SLOT = 1L;
    private static final Long UPDATED_MAX_SLOT = 2L;

    private static final Long DEFAULT_CURRENT_SLOT = 1L;
    private static final Long UPDATED_CURRENT_SLOT = 2L;

    @Autowired
    private CourseSlotStatusRepository courseSlotStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCourseSlotStatusMockMvc;

    private CourseSlotStatus courseSlotStatus;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSlotStatus createEntity() {
        CourseSlotStatus courseSlotStatus = new CourseSlotStatus()
            .courseCode(DEFAULT_COURSE_CODE)
            .maxSlot(DEFAULT_MAX_SLOT)
            .currentSlot(DEFAULT_CURRENT_SLOT);
        return courseSlotStatus;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSlotStatus createUpdatedEntity() {
        CourseSlotStatus courseSlotStatus = new CourseSlotStatus()
            .courseCode(UPDATED_COURSE_CODE)
            .maxSlot(UPDATED_MAX_SLOT)
            .currentSlot(UPDATED_CURRENT_SLOT);
        return courseSlotStatus;
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseSlotStatusResource courseSlotStatusResource = new CourseSlotStatusResource(courseSlotStatusRepository);
        this.restCourseSlotStatusMockMvc = MockMvcBuilders.standaloneSetup(courseSlotStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @BeforeEach
    public void initTest() {
        courseSlotStatusRepository.deleteAll();
        courseSlotStatus = createEntity();
    }

    @Test
    public void createCourseSlotStatus() throws Exception {
        int databaseSizeBeforeCreate = courseSlotStatusRepository.findAll().size();

        // Create the CourseSlotStatus
        restCourseSlotStatusMockMvc.perform(post("/api/course-slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseSlotStatus)))
            .andExpect(status().isCreated());

        // Validate the CourseSlotStatus in the database
        List<CourseSlotStatus> courseSlotStatusList = courseSlotStatusRepository.findAll();
        assertThat(courseSlotStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CourseSlotStatus testCourseSlotStatus = courseSlotStatusList.get(courseSlotStatusList.size() - 1);
        assertThat(testCourseSlotStatus.getCourseCode()).isEqualTo(DEFAULT_COURSE_CODE);
        assertThat(testCourseSlotStatus.getMaxSlot()).isEqualTo(DEFAULT_MAX_SLOT);
        assertThat(testCourseSlotStatus.getCurrentSlot()).isEqualTo(DEFAULT_CURRENT_SLOT);
    }

    @Test
    public void createCourseSlotStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseSlotStatusRepository.findAll().size();

        // Create the CourseSlotStatus with an existing ID
        courseSlotStatus.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseSlotStatusMockMvc.perform(post("/api/course-slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseSlotStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CourseSlotStatus in the database
        List<CourseSlotStatus> courseSlotStatusList = courseSlotStatusRepository.findAll();
        assertThat(courseSlotStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllCourseSlotStatuses() throws Exception {
        // Initialize the database
        courseSlotStatus.setId(UUID.randomUUID());
        courseSlotStatusRepository.save(courseSlotStatus);

        // Get all the courseSlotStatusList
        restCourseSlotStatusMockMvc.perform(get("/api/course-slot-statuses"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseSlotStatus.getId().toString())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE)))
            .andExpect(jsonPath("$.[*].maxSlot").value(hasItem(DEFAULT_MAX_SLOT.intValue())))
            .andExpect(jsonPath("$.[*].currentSlot").value(hasItem(DEFAULT_CURRENT_SLOT.intValue())));
    }

    @Test
    public void getCourseSlotStatus() throws Exception {
        // Initialize the database
        courseSlotStatus.setId(UUID.randomUUID());
        courseSlotStatusRepository.save(courseSlotStatus);

        // Get the courseSlotStatus
        restCourseSlotStatusMockMvc.perform(get("/api/course-slot-statuses/{id}", courseSlotStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseSlotStatus.getId().toString()))
            .andExpect(jsonPath("$.courseCode").value(DEFAULT_COURSE_CODE))
            .andExpect(jsonPath("$.maxSlot").value(DEFAULT_MAX_SLOT.intValue()))
            .andExpect(jsonPath("$.currentSlot").value(DEFAULT_CURRENT_SLOT.intValue()));
    }

    @Test
    public void getNonExistingCourseSlotStatus() throws Exception {
        // Get the courseSlotStatus
        restCourseSlotStatusMockMvc.perform(get("/api/course-slot-statuses/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCourseSlotStatus() throws Exception {
        // Initialize the database
        courseSlotStatus.setId(UUID.randomUUID());
        courseSlotStatusRepository.save(courseSlotStatus);

        int databaseSizeBeforeUpdate = courseSlotStatusRepository.findAll().size();

        // Update the courseSlotStatus
        CourseSlotStatus updatedCourseSlotStatus = courseSlotStatusRepository.findById(courseSlotStatus.getId()).get();
        updatedCourseSlotStatus
            .courseCode(UPDATED_COURSE_CODE)
            .maxSlot(UPDATED_MAX_SLOT)
            .currentSlot(UPDATED_CURRENT_SLOT);

        restCourseSlotStatusMockMvc.perform(put("/api/course-slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseSlotStatus)))
            .andExpect(status().isOk());

        // Validate the CourseSlotStatus in the database
        List<CourseSlotStatus> courseSlotStatusList = courseSlotStatusRepository.findAll();
        assertThat(courseSlotStatusList).hasSize(databaseSizeBeforeUpdate);
        CourseSlotStatus testCourseSlotStatus = courseSlotStatusList.get(courseSlotStatusList.size() - 1);
        assertThat(testCourseSlotStatus.getCourseCode()).isEqualTo(UPDATED_COURSE_CODE);
        assertThat(testCourseSlotStatus.getMaxSlot()).isEqualTo(UPDATED_MAX_SLOT);
        assertThat(testCourseSlotStatus.getCurrentSlot()).isEqualTo(UPDATED_CURRENT_SLOT);
    }

    @Test
    public void updateNonExistingCourseSlotStatus() throws Exception {
        int databaseSizeBeforeUpdate = courseSlotStatusRepository.findAll().size();

        // Create the CourseSlotStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSlotStatusMockMvc.perform(put("/api/course-slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseSlotStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CourseSlotStatus in the database
        List<CourseSlotStatus> courseSlotStatusList = courseSlotStatusRepository.findAll();
        assertThat(courseSlotStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourseSlotStatus() throws Exception {
        // Initialize the database
        courseSlotStatus.setId(UUID.randomUUID());
        courseSlotStatusRepository.save(courseSlotStatus);

        int databaseSizeBeforeDelete = courseSlotStatusRepository.findAll().size();

        // Delete the courseSlotStatus
        restCourseSlotStatusMockMvc.perform(delete("/api/course-slot-statuses/{id}", courseSlotStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseSlotStatus> courseSlotStatusList = courseSlotStatusRepository.findAll();
        assertThat(courseSlotStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
