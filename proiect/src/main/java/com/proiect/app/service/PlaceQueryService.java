package com.proiect.app.service;

import com.proiect.app.domain.*; // for static metamodels
import com.proiect.app.domain.Place;
import com.proiect.app.repository.PlaceRepository;
import com.proiect.app.service.criteria.PlaceCriteria;
import com.proiect.app.service.dto.PlaceDTO;
import com.proiect.app.service.mapper.PlaceMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Place} entities in the database.
 * The main input is a {@link PlaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlaceDTO} or a {@link Page} of {@link PlaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaceQueryService extends QueryService<Place> {

    private final Logger log = LoggerFactory.getLogger(PlaceQueryService.class);

    private final PlaceRepository placeRepository;

    private final PlaceMapper placeMapper;

    public PlaceQueryService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    /**
     * Return a {@link List} of {@link PlaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlaceDTO> findByCriteria(PlaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Place> specification = createSpecification(criteria);
        return placeMapper.toDto(placeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaceDTO> findByCriteria(PlaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Place> specification = createSpecification(criteria);
        return placeRepository.findAll(specification, page).map(placeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Place> specification = createSpecification(criteria);
        return placeRepository.count(specification);
    }

    /**
     * Function to convert {@link PlaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Place> createSpecification(PlaceCriteria criteria) {
        Specification<Place> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Place_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Place_.name));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Place_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Place_.longitude));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Place_.address));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Place_.type));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Place_.description));
            }
        }
        return specification;
    }
}
