package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.SubjectRequire;
import com.wlminus.ufp.repository.SubjectRequireRepository;
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
 * REST controller for managing {@link com.wlminus.ufp.domain.SubjectRequire}.
 */
@RestController
@RequestMapping("/api")
public class SubjectRequireResource {

    private static final String ENTITY_NAME = "subjectRequire";
    private final Logger log = LoggerFactory.getLogger(SubjectRequireResource.class);
    private final SubjectRequireRepository subjectRequireRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public SubjectRequireResource(SubjectRequireRepository subjectRequireRepository) {
        this.subjectRequireRepository = subjectRequireRepository;
    }

    /**
     * {@code POST  /subject-requires} : Create a new subjectRequire.
     *
     * @param subjectRequire the subjectRequire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subjectRequire, or with status {@code 400 (Bad Request)} if the subjectRequire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subject-requires")
    public ResponseEntity<SubjectRequire> createSubjectRequire(@RequestBody SubjectRequire subjectRequire) throws URISyntaxException {
        log.debug("REST request to save SubjectRequire : {}", subjectRequire);
        if (subjectRequire.getId() != null) {
            throw new BadRequestAlertException("A new subjectRequire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subjectRequire.setId(UUID.randomUUID());
        SubjectRequire result = subjectRequireRepository.save(subjectRequire);
        return ResponseEntity.created(new URI("/api/subject-requires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subject-requires} : Updates an existing subjectRequire.
     *
     * @param subjectRequire the subjectRequire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subjectRequire,
     * or with status {@code 400 (Bad Request)} if the subjectRequire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subjectRequire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subject-requires")
    public ResponseEntity<SubjectRequire> updateSubjectRequire(@RequestBody SubjectRequire subjectRequire) throws URISyntaxException {
        log.debug("REST request to update SubjectRequire : {}", subjectRequire);
        if (subjectRequire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubjectRequire result = subjectRequireRepository.save(subjectRequire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subjectRequire.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subject-requires} : get all the subjectRequires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subjectRequires in body.
     */
    @GetMapping("/subject-requires")
    public List<SubjectRequire> getAllSubjectRequires() {
        log.debug("REST request to get all SubjectRequires");
        return subjectRequireRepository.findAll();
    }

    /**
     * {@code GET  /subject-requires/:id} : get the "id" subjectRequire.
     *
     * @param id the id of the subjectRequire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subjectRequire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subject-requires/{id}")
    public ResponseEntity<SubjectRequire> getSubjectRequire(@PathVariable UUID id) {
        log.debug("REST request to get SubjectRequire : {}", id);
        Optional<SubjectRequire> subjectRequire = subjectRequireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subjectRequire);
    }

    /**
     * {@code DELETE  /subject-requires/:id} : delete the "id" subjectRequire.
     *
     * @param id the id of the subjectRequire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subject-requires/{id}")
    public ResponseEntity<Void> deleteSubjectRequire(@PathVariable UUID id) {
        log.debug("REST request to delete SubjectRequire : {}", id);
        subjectRequireRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
