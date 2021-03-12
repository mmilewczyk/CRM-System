package com.agiklo.oracledatabase.mapper;

import com.agiklo.oracledatabase.entity.Comment;
import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.CommentDTO;
import com.agiklo.oracledatabase.entity.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "postId", source = "postId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    PostDTO mapPostToDTO(Post post);

    @Mapping(target = "comments", source = "comments")
    Set<CommentDTO> mapCommentsToDTO(Set<Comment> comments);
}
