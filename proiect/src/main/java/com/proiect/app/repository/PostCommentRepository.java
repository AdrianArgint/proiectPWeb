package com.proiect.app.repository;

import com.proiect.app.domain.PostComment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PostComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long>, JpaSpecificationExecutor<PostComment> {
    @Query("select postComment from PostComment postComment where postComment.author.login = ?#{principal.preferredUsername}")
    List<PostComment> findByAuthorIsCurrentUser();
}
