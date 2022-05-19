package com.proiect.app.service;

import com.proiect.app.domain.*; // for static metamodels
import com.proiect.app.domain.PostComment;
import com.proiect.app.repository.PostCommentRepository;
import com.proiect.app.service.criteria.PostCommentCriteria;
import com.proiect.app.service.dto.PostCommentDTO;
import com.proiect.app.service.mapper.PostCommentMapper;
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
 * Service for executing complex queries for {@link PostComment} entities in the database.
 * The main input is a {@link PostCommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostCommentDTO} or a {@link Page} of {@link PostCommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostCommentQueryService extends QueryService<PostComment> {

    private final Logger log = LoggerFactory.getLogger(PostCommentQueryService.class);

    private final PostCommentRepository postCommentRepository;

    private final PostCommentMapper postCommentMapper;

    public PostCommentQueryService(PostCommentRepository postCommentRepository, PostCommentMapper postCommentMapper) {
        this.postCommentRepository = postCommentRepository;
        this.postCommentMapper = postCommentMapper;
    }

    /**
     * Return a {@link List} of {@link PostCommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostCommentDTO> findByCriteria(PostCommentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PostComment> specification = createSpecification(criteria);
        return postCommentMapper.toDto(postCommentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostCommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostCommentDTO> findByCriteria(PostCommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PostComment> specification = createSpecification(criteria);
        return postCommentRepository.findAll(specification, page).map(postCommentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostCommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PostComment> specification = createSpecification(criteria);
        return postCommentRepository.count(specification);
    }

    /**
     * Function to convert {@link PostCommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PostComment> createSpecification(PostCommentCriteria criteria) {
        Specification<PostComment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PostComment_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), PostComment_.content));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), PostComment_.createdDate));
            }
            if (criteria.getLastUpdatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastUpdatedDate(), PostComment_.lastUpdatedDate));
            }
            if (criteria.getPostId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPostId(), root -> root.join(PostComment_.post, JoinType.LEFT).get(Post_.id))
                    );
            }
            if (criteria.getAuthorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAuthorId(), root -> root.join(PostComment_.author, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
