package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.StudentRegisterStatus;
import com.wlminus.ufp.repository.StudentRegisterStatusRepository;
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
 * REST controller for managing {@link com.wlminus.ufp.domain.StudentRegisterStatus}.
 */
@RestController
@RequestMapping("/api")
public class StudentRegisterStatusResource {

    private static final String ENTITY_NAME = "studentRegisterStatus";
    private final Logger log = LoggerFactory.getLogger(StudentRegisterStatusResource.class);
    private final StudentRegisterStatusRepository studentRegisterStatusRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public StudentRegisterStatusResource(StudentRegisterStatusRepository studentRegisterStatusRepository) {
        this.studentRegisterStatusRepository = studentRegisterStatusRepository;
    }

    /**
     * {@code POST  /student-register-statuses} : Create a new studentRegisterStatus.
     *
     * @param studentRegisterStatus the studentRegisterStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentRegisterStatus, or with status {@code 400 (Bad Request)} if the studentRegisterStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-register-statuses")
    public ResponseEntity<StudentRegisterStatus> createStudentRegisterStatus(@RequestBody StudentRegisterStatus studentRegisterStatus) throws URISyntaxException {
        log.debug("REST request to save StudentRegisterStatus : {}", studentRegisterStatus);
        if (studentRegisterStatus.getId() != null) {
            throw new BadRequestAlertException("A new studentRegisterStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentRegisterStatus.setId(UUID.randomUUID());
        StudentRegisterStatus result = studentRegisterStatusRepository.save(studentRegisterStatus);
        return ResponseEntity.created(new URI("/api/student-register-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-register-statuses} : Updates an existing studentRegisterStatus.
     *
     * @param studentRegisterStatus the studentRegisterStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRegisterStatus,
     * or with status {@code 400 (Bad Request)} if the studentRegisterStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentRegisterStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-register-statuses")
    public ResponseEntity<StudentRegisterStatus> updateStudentRegisterStatus(@RequestBody StudentRegisterStatus studentRegisterStatus) throws URISyntaxException {
        log.debug("REST request to update StudentRegisterStatus : {}", studentRegisterStatus);
        if (studentRegisterStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentRegisterStatus result = studentRegisterStatusRepository.save(studentRegisterStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentRegisterStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /student-register-statuses} : get all the studentRegisterStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentRegisterStatuses in body.
     */
    @GetMapping("/student-register-statuses")
    public List<StudentRegisterStatus> getAllStudentRegisterStatuses() {
        log.debug("REST request to get all StudentRegisterStatuses");
        return studentRegisterStatusRepository.findAll();
    }

    /**
     * {@code GET  /student-register-statuses/:id} : get the "id" studentRegisterStatus.
     *
     * @param id the id of the studentRegisterStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentRegisterStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-register-statuses/{id}")
    public ResponseEntity<StudentRegisterStatus> getStudentRegisterStatus(@PathVariable UUID id) {
        log.debug("REST request to get StudentRegisterStatus : {}", id);
        Optional<StudentRegisterStatus> studentRegisterStatus = studentRegisterStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentRegisterStatus);
    }

    /**
     * {@code DELETE  /student-register-statuses/:id} : delete the "id" studentRegisterStatus.
     *
     * @param id the id of the studentRegisterStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-register-statuses/{id}")
    public ResponseEntity<Void> deleteStudentRegisterStatus(@PathVariable UUID id) {
        log.debug("REST request to delete StudentRegisterStatus : {}", id);
        studentRegisterStatusRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
