package com.wlminus.ufp.web.rest;

import com.wlminus.ufp.domain.RequirePair;
import com.wlminus.ufp.repository.RequirePairRepository;
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
 * REST controller for managing {@link com.wlminus.ufp.domain.RequirePair}.
 */
@RestController
@RequestMapping("/api")
public class RequirePairResource {

    private static final String ENTITY_NAME = "requirePair";
    private final Logger log = LoggerFactory.getLogger(RequirePairResource.class);
    private final RequirePairRepository requirePairRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public RequirePairResource(RequirePairRepository requirePairRepository) {
        this.requirePairRepository = requirePairRepository;
    }

    /**
     * {@code POST  /require-pairs} : Create a new requirePair.
     *
     * @param requirePair the requirePair to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requirePair, or with status {@code 400 (Bad Request)} if the requirePair has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/require-pairs")
    public ResponseEntity<RequirePair> createRequirePair(@RequestBody RequirePair requirePair) throws URISyntaxException {
        log.debug("REST request to save RequirePair : {}", requirePair);
        if (requirePair.getId() != null) {
            throw new BadRequestAlertException("A new requirePair cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requirePair.setId(UUID.randomUUID());
        RequirePair result = requirePairRepository.save(requirePair);
        return ResponseEntity.created(new URI("/api/require-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /require-pairs} : Updates an existing requirePair.
     *
     * @param requirePair the requirePair to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requirePair,
     * or with status {@code 400 (Bad Request)} if the requirePair is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requirePair couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/require-pairs")
    public ResponseEntity<RequirePair> updateRequirePair(@RequestBody RequirePair requirePair) throws URISyntaxException {
        log.debug("REST request to update RequirePair : {}", requirePair);
        if (requirePair.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequirePair result = requirePairRepository.save(requirePair);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requirePair.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /require-pairs} : get all the requirePairs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requirePairs in body.
     */
    @GetMapping("/require-pairs")
    public List<RequirePair> getAllRequirePairs() {
        log.debug("REST request to get all RequirePairs");
        return requirePairRepository.findAll();
    }

    /**
     * {@code GET  /require-pairs/:id} : get the "id" requirePair.
     *
     * @param id the id of the requirePair to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requirePair, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/require-pairs/{id}")
    public ResponseEntity<RequirePair> getRequirePair(@PathVariable UUID id) {
        log.debug("REST request to get RequirePair : {}", id);
        Optional<RequirePair> requirePair = requirePairRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requirePair);
    }

    /**
     * {@code DELETE  /require-pairs/:id} : delete the "id" requirePair.
     *
     * @param id the id of the requirePair to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/require-pairs/{id}")
    public ResponseEntity<Void> deleteRequirePair(@PathVariable UUID id) {
        log.debug("REST request to delete RequirePair : {}", id);
        requirePairRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
