package com.proiect.app.web.rest;

import com.proiect.app.repository.PlaceRepository;
import com.proiect.app.service.PlaceQueryService;
import com.proiect.app.service.PlaceService;
import com.proiect.app.service.criteria.PlaceCriteria;
import com.proiect.app.service.dto.PlaceDTO;
import com.proiect.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.proiect.app.domain.Place}.
 */
@RestController
@RequestMapping("/api")
public class PlaceResource {

    private final Logger log = LoggerFactory.getLogger(PlaceResource.class);

    private static final String ENTITY_NAME = "place";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceService placeService;

    private final PlaceRepository placeRepository;

    private final PlaceQueryService placeQueryService;

    public PlaceResource(PlaceService placeService, PlaceRepository placeRepository, PlaceQueryService placeQueryService) {
        this.placeService = placeService;
        this.placeRepository = placeRepository;
        this.placeQueryService = placeQueryService;
    }

    /**
     * {@code POST  /places} : Create a new place.
     *
     * @param placeDTO the placeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new placeDTO, or with status {@code 400 (Bad Request)} if the place has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/places")
    public ResponseEntity<PlaceDTO> createPlace(@RequestBody PlaceDTO placeDTO) throws URISyntaxException {
        log.debug("REST request to save Place : {}", placeDTO);
        if (placeDTO.getId() != null) {
            throw new BadRequestAlertException("A new place cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlaceDTO result = placeService.save(placeDTO);
        return ResponseEntity
            .created(new URI("/api/places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /places/:id} : Updates an existing place.
     *
     * @param id the id of the placeDTO to save.
     * @param placeDTO the placeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeDTO,
     * or with status {@code 400 (Bad Request)} if the placeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the placeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/places/{id}")
    public ResponseEntity<PlaceDTO> updatePlace(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlaceDTO placeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Place : {}, {}", id, placeDTO);
        if (placeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlaceDTO result = placeService.save(placeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, placeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /places/:id} : Partial updates given fields of an existing place, field will ignore if it is null
     *
     * @param id the id of the placeDTO to save.
     * @param placeDTO the placeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeDTO,
     * or with status {@code 400 (Bad Request)} if the placeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the placeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the placeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/places/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaceDTO> partialUpdatePlace(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlaceDTO placeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Place partially : {}, {}", id, placeDTO);
        if (placeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaceDTO> result = placeService.partialUpdate(placeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, placeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /places} : get all the places.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of places in body.
     */
    @GetMapping("/places")
    public ResponseEntity<List<PlaceDTO>> getAllPlaces(
        PlaceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Places by criteria: {}", criteria);
        Page<PlaceDTO> page = placeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /places/count} : count all the places.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/places/count")
    public ResponseEntity<Long> countPlaces(PlaceCriteria criteria) {
        log.debug("REST request to count Places by criteria: {}", criteria);
        return ResponseEntity.ok().body(placeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /places/:id} : get the "id" place.
     *
     * @param id the id of the placeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the placeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/places/{id}")
    public ResponseEntity<PlaceDTO> getPlace(@PathVariable Long id) {
        log.debug("REST request to get Place : {}", id);
        Optional<PlaceDTO> placeDTO = placeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeDTO);
    }

    /**
     * {@code DELETE  /places/:id} : delete the "id" place.
     *
     * @param id the id of the placeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        log.debug("REST request to delete Place : {}", id);
        placeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
