package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.PassStatus;
import com.wlminus.ufp.repository.PassStatusRepository;
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
 * REST controller for managing {@link com.wlminus.ufp.domain.PassStatus}.
 */
@RestController
@RequestMapping("/api")
public class PassStatusResource {

    private static final String ENTITY_NAME = "passStatus";
    private final Logger log = LoggerFactory.getLogger(PassStatusResource.class);
    private final PassStatusRepository passStatusRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PassStatusResource(PassStatusRepository passStatusRepository) {
        this.passStatusRepository = passStatusRepository;
    }

    /**
     * {@code POST  /pass-statuses} : Create a new passStatus.
     *
     * @param passStatus the passStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passStatus, or with status {@code 400 (Bad Request)} if the passStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pass-statuses")
    public ResponseEntity<PassStatus> createPassStatus(@RequestBody PassStatus passStatus) throws URISyntaxException {
        log.debug("REST request to save PassStatus : {}", passStatus);
        if (passStatus.getId() != null) {
            throw new BadRequestAlertException("A new passStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        passStatus.setId(UUID.randomUUID());
        PassStatus result = passStatusRepository.save(passStatus);
        return ResponseEntity.created(new URI("/api/pass-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pass-statuses} : Updates an existing passStatus.
     *
     * @param passStatus the passStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passStatus,
     * or with status {@code 400 (Bad Request)} if the passStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pass-statuses")
    public ResponseEntity<PassStatus> updatePassStatus(@RequestBody PassStatus passStatus) throws URISyntaxException {
        log.debug("REST request to update PassStatus : {}", passStatus);
        if (passStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PassStatus result = passStatusRepository.save(passStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pass-statuses} : get all the passStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passStatuses in body.
     */
    @GetMapping("/pass-statuses")
    public List<PassStatus> getAllPassStatuses() {
        log.debug("REST request to get all PassStatuses");
        return passStatusRepository.findAll();
    }

    /**
     * {@code GET  /pass-statuses/:id} : get the "id" passStatus.
     *
     * @param id the id of the passStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pass-statuses/{id}")
    public ResponseEntity<PassStatus> getPassStatus(@PathVariable UUID id) {
        log.debug("REST request to get PassStatus : {}", id);
        Optional<PassStatus> passStatus = passStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(passStatus);
    }

    /**
     * {@code DELETE  /pass-statuses/:id} : delete the "id" passStatus.
     *
     * @param id the id of the passStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pass-statuses/{id}")
    public ResponseEntity<Void> deletePassStatus(@PathVariable UUID id) {
        log.debug("REST request to delete PassStatus : {}", id);
        passStatusRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
