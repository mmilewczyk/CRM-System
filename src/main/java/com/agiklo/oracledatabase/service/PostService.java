package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.PostDTO;
import com.agiklo.oracledatabase.enums.USER_ROLE;
import com.agiklo.oracledatabase.mapper.PostMapper;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import com.agiklo.oracledatabase.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final EmployeeRepository employeeRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapPostToDTO)
                .collect(Collectors.toList());
    }

    public Post addNewPost(Post post, Principal principal) {
        Employee employee = employeeRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalStateException("Employee not found"));
        LocalDateTime actualTime = LocalDateTime.now();
        post.setAuthor(employee);
        post.setCreatedAt(actualTime);
        return postRepository.save(post);
    }

    /**
     * the method is to check if the logged in user is the author of the post or the admin,
     * if everything is correct, it deletes the selected post by id.
     * @param id id of the post to be deleted
     * @param principal logged in user
     * @throws ResponseStatusException if principal is not the author of the post or the admin throws 403 status with message,
     *                                 if id of the post is incorrect throws 404 status with message
     * @throws IllegalStateException   if user is not logged in
     */
    public void deletePostById(Long id, Principal principal) {
        Post post = postRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post cannot be found, the specified id does not exist"));
        Employee employee = employeeRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalStateException("Employee not found"));
        if(!employee.getUserRole().equals(USER_ROLE.ADMIN)){
            if (post.getAuthor().getEmail().equals(principal.getName())) {
                postRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this post");
            }
        } else {
            postRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return postMapper.mapPostToDTO(post);
    }

    @Transactional(readOnly = true)
    public Set<PostDTO> findPostsByAuthorFirstnameAndLastname(String firstName, String lastName) {
            return postRepository.findPostByAuthorFirstNameAndAuthorLastName(firstName, lastName)
                    .stream()
                    .map(postMapper::mapPostToDTO)
                    .collect(Collectors.toSet());
    }
}
