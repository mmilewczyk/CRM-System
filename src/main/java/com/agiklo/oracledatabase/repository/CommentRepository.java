package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"post", "author"})
    List<Comment> getCommentsByPost_PostId(Long id, Pageable pageable);

    @EntityGraph(attributePaths = {"post", "author"})
    List<Comment> findAllBy(Pageable pageable);
}
