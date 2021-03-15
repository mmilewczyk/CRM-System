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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsByPostId(Long id){
        return commentRepository.getCommentsByPost_PostId(id)
                .stream()
                .map(commentMapper::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getAllComments(){
        return commentRepository.findAll()
                .stream()
                .map(commentMapper::mapCommentToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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

    public void deleteCommentInPostById(Long id, Principal principal) {
        Comment comment = commentRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(NOT_FOUND, "Comment cannot be found, the specified id does not exist"));
        if (comment.getAuthor().getEmail().equals(principal.getName())) {
            commentRepository.deleteById(id);
        }
        else {
            throw new ResponseStatusException(FORBIDDEN, "You are not the author of this comment");
        }
    }
}
