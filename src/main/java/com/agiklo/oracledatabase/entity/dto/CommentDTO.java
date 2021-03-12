package com.agiklo.oracledatabase.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private Long postId;
    private String createdAt;
    private String commentAuthorFirstName;
    private String commentAuthorLastName;
    private String content;
}
