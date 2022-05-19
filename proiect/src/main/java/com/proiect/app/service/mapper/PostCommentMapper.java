package com.proiect.app.service.mapper;

import com.proiect.app.domain.PostComment;
import com.proiect.app.service.dto.PostCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostComment} and its DTO {@link PostCommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PostMapper.class, UserMapper.class })
public interface PostCommentMapper extends EntityMapper<PostCommentDTO, PostComment> {
    @Mapping(target = "post", source = "post", qualifiedByName = "id")
    @Mapping(target = "author", source = "author", qualifiedByName = "login")
    PostCommentDTO toDto(PostComment s);
}
