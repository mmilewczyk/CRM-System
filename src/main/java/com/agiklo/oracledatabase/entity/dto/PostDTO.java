package com.agiklo.oracledatabase.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    private Long postId;
    private String createdAt;
    private String authorFirstName;
    private String authorLastName;
    private String title;
    private String content;
    private Set<CommentDTO> comments;

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }


}
