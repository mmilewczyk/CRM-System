package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.PostDTO;
import com.agiklo.oracledatabase.mapper.PostMapper;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import com.agiklo.oracledatabase.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final PostRepository postRepository;
    private final EmployeeRepository employeeRepository;
    private final PostMapper postMapper;

    /**
     * The method is to retrieve all posts from the database and display them.
     *
     * After downloading all the data about the post,
     * the data is mapped to dto which will display only those needed
     *
     * @return list of all posts with specification of data in PostDTO
     */
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts(Pageable pageable){
        return postRepository.findAllBy(pageable)
                .stream()
                .map(postMapper::mapPostToDTO)
                .collect(Collectors.toList());
    }

    /**
     * The task of the method is to add a post to the database with the appropriate data.
     *
     * The method retrieves the data of the logged in user and
     * requestbody of the post. The method then sets principal
     * as author and today's date on the post.
     * @param post requestbody of the post to be saved
     * @param principal logged in user
     *
     * @throws IllegalStateException if user is not logged in
     *
     * @return saving the post to the database
     */
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
        if(employee.isAdmin() || isAuthorOfPost(post, principal)){
            postRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this post");
        }
    }

    /**
     * The method is to download a specific post from the database and display it.
     * After downloading all the data about the post,
     * the data is mapped to dto which will display only those needed
     *
     * @param id id of the post to be searched for
     * @throws ResponseStatusException if the id of the post you are looking for does not exist
     * @return detailed data about a specific post
     */
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return postMapper.mapPostToDTO(post);
    }

    /**
     * The method is to retrieve posts whose authors have the name and surname specified by the user.
     * After downloading all the data about the post,
     * the data is mapped to dto which will display only those needed
     * @param firstName firstname of the author
     * @param lastName lastname of the author
     * @return details of specific posts
     */
    @Transactional(readOnly = true)
    public Set<PostDTO> findPostsByAuthorFirstnameAndLastname(String firstName, String lastName, Pageable pageable) {
            return postRepository.findPostByAuthorFirstNameAndAuthorLastName(firstName, lastName, pageable)
                    .stream()
                    .map(postMapper::mapPostToDTO)
                    .collect(Collectors.toSet());
    }

    /**
     * Method enabling editing of the selected post, the editor must be the author of the post.
     * @param post requestbody of the post to be edited
     * @param principal logged in user
     * @return edited post
     */
    @Transactional
    public PostDTO editPostContent(Post post, Principal principal){
        Post editedPost = postRepository.findById(post.getPostId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Post does not exist"));
        if(isAuthorOfPost(editedPost, principal)) {
            editedPost.setTitle(post.getTitle());
            editedPost.setContent(post.getContent());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this post");
        }
        return postMapper.mapPostToDTO(editedPost);
    }

    /**
     * The method checks if the logged in user is the author of the post.
     * @param post post whose author is to be checked
     * @param principal logged in user
     * @return true if principal is author or false if not
     */
    private boolean isAuthorOfPost(Post post, Principal principal){
        return post.getAuthor().getEmail().equals(principal.getName());
    }
}
