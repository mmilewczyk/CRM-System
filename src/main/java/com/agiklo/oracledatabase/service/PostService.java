package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import com.agiklo.oracledatabase.repository.PostRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post addNewPost(Post post, Principal principal) {
        Employee employee = employeeRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalStateException("Employee not found"));
        LocalDateTime actualTime = LocalDateTime.now();
        post.setAuthor(employee);
        post.setCreatedAt(actualTime);
        return postRepository.save(post);
    }

    public void deletePostById(Long id) throws NotFoundException {
        try{
            postRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified id does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Set<Post> findPostsByAuthorFirstnameAndLastname(String firstName, String lastName) throws NotFoundException {
        try {
            return postRepository.findPostByAuthorFirstNameAndAuthorLastName(firstName, lastName);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("The specified post does not exist");
        }
    }
}
