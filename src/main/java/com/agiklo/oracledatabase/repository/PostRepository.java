package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostByAuthorFirstNameAndAuthorLastName(String firstname, String lastName, Pageable pageable);

}
