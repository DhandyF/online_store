package org.jhipster.blog.web.rest;
import org.jhipster.blog.domain.Points;
import org.jhipster.blog.repository.PointsRepository;
import org.jhipster.blog.repository.search.PointsSearchRepository;
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
 * REST controller for managing Points.
 */
@RestController
@RequestMapping("/api")
public class PointsResource {

    private final Logger log = LoggerFactory.getLogger(PointsResource.class);

    private static final String ENTITY_NAME = "points";

    private final PointsRepository pointsRepository;

    private final PointsSearchRepository pointsSearchRepository;

    public PointsResource(PointsRepository pointsRepository, PointsSearchRepository pointsSearchRepository) {
        this.pointsRepository = pointsRepository;
        this.pointsSearchRepository = pointsSearchRepository;
    }

    /**
     * POST  /points : Create a new points.
     *
     * @param points the points to create
     * @return the ResponseEntity with status 201 (Created) and with body the new points, or with status 400 (Bad Request) if the points has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/points")
    public ResponseEntity<Points> createPoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to save Points : {}", points);
        if (points.getId() != null) {
            throw new BadRequestAlertException("A new points cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Points result = pointsRepository.save(points);
        pointsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /points : Updates an existing points.
     *
     * @param points the points to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated points,
     * or with status 400 (Bad Request) if the points is not valid,
     * or with status 500 (Internal Server Error) if the points couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/points")
    public ResponseEntity<Points> updatePoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to update Points : {}", points);
        if (points.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Points result = pointsRepository.save(points);
        pointsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, points.getId().toString()))
            .body(result);
    }

    /**
     * GET  /points : get all the points.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of points in body
     */
    @GetMapping("/points")
    public ResponseEntity<List<Points>> getAllPoints(Pageable pageable) {
        log.debug("REST request to get a page of Points");
        Page<Points> page = pointsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/points");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /points/:id : get the "id" points.
     *
     * @param id the id of the points to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the points, or with status 404 (Not Found)
     */
    @GetMapping("/points/{id}")
    public ResponseEntity<Points> getPoints(@PathVariable Long id) {
        log.debug("REST request to get Points : {}", id);
        Optional<Points> points = pointsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(points);
    }

    /**
     * DELETE  /points/:id : delete the "id" points.
     *
     * @param id the id of the points to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/points/{id}")
    public ResponseEntity<Void> deletePoints(@PathVariable Long id) {
        log.debug("REST request to delete Points : {}", id);
        pointsRepository.deleteById(id);
        pointsSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/points?query=:query : search for the points corresponding
     * to the query.
     *
     * @param query the query of the points search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/points")
    public ResponseEntity<List<Points>> searchPoints(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Points for query {}", query);
        Page<Points> page = pointsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/points");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
