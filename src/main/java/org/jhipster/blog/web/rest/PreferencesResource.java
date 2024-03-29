package org.jhipster.blog.web.rest;
import org.jhipster.blog.domain.Preferences;
import org.jhipster.blog.repository.PreferencesRepository;
import org.jhipster.blog.repository.search.PreferencesSearchRepository;
import org.jhipster.blog.web.rest.errors.BadRequestAlertException;
import org.jhipster.blog.web.rest.util.HeaderUtil;
import org.jhipster.blog.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Preferences.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);

    private static final String ENTITY_NAME = "preferences";

    private final PreferencesRepository preferencesRepository;

    private final PreferencesSearchRepository preferencesSearchRepository;

    public PreferencesResource(PreferencesRepository preferencesRepository, PreferencesSearchRepository preferencesSearchRepository) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesSearchRepository = preferencesSearchRepository;
    }

    /**
     * POST  /preferences : Create a new preferences.
     *
     * @param preferences the preferences to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preferences, or with status 400 (Bad Request) if the preferences has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/preferences")
    public ResponseEntity<Preferences> createPreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferences);
        if (preferences.getId() != null) {
            throw new BadRequestAlertException("A new preferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Preferences result = preferencesRepository.save(preferences);
        preferencesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferences : Updates an existing preferences.
     *
     * @param preferences the preferences to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preferences,
     * or with status 400 (Bad Request) if the preferences is not valid,
     * or with status 500 (Internal Server Error) if the preferences couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/preferences")
    public ResponseEntity<Preferences> updatePreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferences);
        if (preferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Preferences result = preferencesRepository.save(preferences);
        preferencesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preferences.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferences : get all the preferences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of preferences in body
     */
    @GetMapping("/preferences")
    public ResponseEntity<List<Preferences>> getAllPreferences(Pageable pageable) {
        log.debug("REST request to get a page of Preferences");
        Page<Preferences> page = preferencesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/preferences");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /preferences/:id : get the "id" preferences.
     *
     * @param id the id of the preferences to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preferences, or with status 404 (Not Found)
     */
    @GetMapping("/preferences/{id}")
    public ResponseEntity<Preferences> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Optional<Preferences> preferences = preferencesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(preferences);
    }

    /**
     * DELETE  /preferences/:id : delete the "id" preferences.
     *
     * @param id the id of the preferences to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesRepository.deleteById(id);
        preferencesSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/preferences?query=:query : search for the preferences corresponding
     * to the query.
     *
     * @param query the query of the preferences search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/preferences")
    public ResponseEntity<List<Preferences>> searchPreferences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Preferences for query {}", query);
        Page<Preferences> page = preferencesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/preferences");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
