package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Comment;
import com.agiklo.oracledatabase.entity.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    CommentDTO mapCommentToDTO(Comment comment);
}
