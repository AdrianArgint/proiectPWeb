package com.proiect.app.repository;

import com.proiect.app.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Post entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Query("select post from Post post where post.author.login = ?#{principal.preferredUsername}")
    List<Post> findByAuthorIsCurrentUser();
}
