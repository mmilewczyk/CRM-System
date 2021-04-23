package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author","comments"})
    List<Post> findPostByAuthorFirstNameAndAuthorLastName(String firstname, String lastName, Pageable pageable);

    @EntityGraph(attributePaths = {"author","comments"})
    List<Post> findAllBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author","comments"})
    Optional<Post> findById(Long aLong);
}
