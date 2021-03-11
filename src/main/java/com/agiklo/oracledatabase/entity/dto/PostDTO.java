package com.agiklo.oracledatabase.entity.dto;

import com.agiklo.oracledatabase.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {

    private String authorFirstName;
    private String authorLastName;
    private String title;
    private String content;
    private List<Comment> comments;

}
