package com.agiklo.oracledatabase.service;

import com.agiklo.oracledatabase.entity.Comment;
import com.agiklo.oracledatabase.entity.Employee;
import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.CommentDTO;
import com.agiklo.oracledatabase.mapper.CommentMapper;
import com.agiklo.oracledatabase.repository.CommentRepository;
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
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Mateusz Milewczyk (agiklo)
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class CommentService {

    /**
     * were injected by the constructor using the lombok @AllArgsContrustor annotation
     */
    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    /**
     * The method is to display all comments that belong to a specific post.
     *
     * After downloading all the data about the comment,
     * the data is mapped to dto which will display only those needed
     * @param id id of the post to which the comments belong
     * @return list of all comments with specification of data in CommentDTO
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsByPostId(Long id, Pageable pageable){
        return commentRepository.getCommentsByPost_PostId(id, pageable)
                .stream()
                .map(commentMapper::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    /**
     * The method is to retrieve all comments from the database and display them.
     *
     * After downloading all the data about the comment,
     * the data is mapped to dto which will display only those needed
     *
     * @return list of all comments with specification of data in CommentDTO
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllComments(Pageable pageable){
        return commentRepository.findAllBy(pageable)
                .stream()
                .map(commentMapper::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    /**
     * The task of the method is to add a comment to a specific post.
     *
     * The method retrieves the data of the post id, logged in user and
     * requestbody of the comment. The method then sets post id, principal
     * as author and today's date on the comment.
     * @param id id of the post to which the comments belong
     * @param comment requestbody of the comment to be saved
     * @param principal logged in user
     *
     * @throws IllegalStateException if user is not logged in
     * @throws ResponseStatusException if post does not exist throws 404 status
     *
     * @return saving the comment to the database
     */
    @Transactional
    public Comment addNewCommentToPost(Long id, Comment comment, Principal principal){
        Post post = postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Post not found"));
        Employee employee = employeeRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalStateException("Employee not found"));
        LocalDateTime actualTime = LocalDateTime.now();
        return commentRepository.save(new Comment(
                employee,
                actualTime,
                comment.getContent(),
                post
        ));
    }

    /**
     * the method is to check if the logged in user is the author of the comment or the admin,
     * if everything is correct, it deletes the selected comment by id.
     * @param id id of the comment to be deleted
     * @param principal logged in user
     * @throws ResponseStatusException if principal is not the author of the comment or the admin throws 403 status with message,
     *                                 if id of the comment is incorrect throws 404 status with message
     * @throws IllegalStateException   if user is not logged in
     */
    public void deleteCommentInPostById(Long id, Principal principal) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(NOT_FOUND, "Comment cannot be found, the specified id does not exist"));
        Employee employee = employeeRepository.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalStateException("Employee not found"));
        if(employee.isAdmin() || isAuthorOfComment(comment, principal)) {
            commentRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(FORBIDDEN, "You are not the author of this comment");
        }
    }

    /**
     * Method enabling editing of the selected comment, the editor must be the author of the comment.
     * @param comment requestbody of the comment to be edited
     * @param principal logged in user
     * @return edited comment
     */
    @Transactional
    public CommentDTO editComment(Comment comment, Principal principal){
        Comment editedComment = commentRepository.findById(comment.getId()).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "Comment does not exist"));
        if(isAuthorOfComment(editedComment, principal)) {
            editedComment.setContent(comment.getContent());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this comment");
        }
        return commentMapper.mapCommentToDTO(editedComment);
    }

    /**
     * The method checks if the logged in user is the author of the comment.
     * @param comment comment whose author is to be checked
     * @param principal logged in user
     * @return true if principal is author or false if not
     */
    private boolean isAuthorOfComment(Comment comment, Principal principal){
        return comment.getAuthor().getEmail().equals(principal.getName());
    }
}
