package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByPost_PostId(Long id);
}
