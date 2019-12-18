package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.CourseSlotStatus;
import com.wlminus.ufp.repository.CourseSlotStatusRepository;
import com.wlminus.ufp.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link com.wlminus.ufp.domain.CourseSlotStatus}.
 */
@RestController
@RequestMapping("/api")
public class CourseSlotStatusResource {

    private static final String ENTITY_NAME = "courseSlotStatus";
    private final Logger log = LoggerFactory.getLogger(CourseSlotStatusResource.class);
    private final CourseSlotStatusRepository courseSlotStatusRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CourseSlotStatusResource(CourseSlotStatusRepository courseSlotStatusRepository) {
        this.courseSlotStatusRepository = courseSlotStatusRepository;
    }

    /**
     * {@code POST  /course-slot-statuses} : Create a new courseSlotStatus.
     *
     * @param courseSlotStatus the courseSlotStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseSlotStatus, or with status {@code 400 (Bad Request)} if the courseSlotStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-slot-statuses")
    public ResponseEntity<CourseSlotStatus> createCourseSlotStatus(@RequestBody CourseSlotStatus courseSlotStatus) throws URISyntaxException {
        log.debug("REST request to save CourseSlotStatus : {}", courseSlotStatus);
        if (courseSlotStatus.getId() != null) {
            throw new BadRequestAlertException("A new courseSlotStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courseSlotStatus.setId(UUID.randomUUID());
        CourseSlotStatus result = courseSlotStatusRepository.save(courseSlotStatus);
        return ResponseEntity.created(new URI("/api/course-slot-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-slot-statuses} : Updates an existing courseSlotStatus.
     *
     * @param courseSlotStatus the courseSlotStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSlotStatus,
     * or with status {@code 400 (Bad Request)} if the courseSlotStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseSlotStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-slot-statuses")
    public ResponseEntity<CourseSlotStatus> updateCourseSlotStatus(@RequestBody CourseSlotStatus courseSlotStatus) throws URISyntaxException {
        log.debug("REST request to update CourseSlotStatus : {}", courseSlotStatus);
        if (courseSlotStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseSlotStatus result = courseSlotStatusRepository.save(courseSlotStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseSlotStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-slot-statuses} : get all the courseSlotStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseSlotStatuses in body.
     */
    @GetMapping("/course-slot-statuses")
    public List<CourseSlotStatus> getAllCourseSlotStatuses() {
        log.debug("REST request to get all CourseSlotStatuses");
        return courseSlotStatusRepository.findAll();
    }

    /**
     * {@code GET  /course-slot-statuses/:id} : get the "id" courseSlotStatus.
     *
     * @param id the id of the courseSlotStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseSlotStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-slot-statuses/{id}")
    public ResponseEntity<CourseSlotStatus> getCourseSlotStatus(@PathVariable UUID id) {
        log.debug("REST request to get CourseSlotStatus : {}", id);
        Optional<CourseSlotStatus> courseSlotStatus = courseSlotStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(courseSlotStatus);
    }

    /**
     * {@code DELETE  /course-slot-statuses/:id} : delete the "id" courseSlotStatus.
     *
     * @param id the id of the courseSlotStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-slot-statuses/{id}")
    public ResponseEntity<Void> deleteCourseSlotStatus(@PathVariable UUID id) {
        log.debug("REST request to delete CourseSlotStatus : {}", id);
        courseSlotStatusRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
