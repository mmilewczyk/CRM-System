package com.agiklo.oracledatabase.repository;

import com.agiklo.oracledatabase.entity.discussions.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Set<Post> findPostByAuthorFirstNameAndAuthorLastName(String firstname, String lastName);
}
