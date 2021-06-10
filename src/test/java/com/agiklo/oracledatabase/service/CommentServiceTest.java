package com.agiklo.oracledatabase.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agiklo.oracledatabase.mapper.CommentMapper;
import com.agiklo.oracledatabase.repository.CommentRepository;
import com.agiklo.oracledatabase.repository.EmployeeRepository;
import com.agiklo.oracledatabase.repository.PostRepository;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CommentService.class})
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class CommentServiceTest {
    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private PostRepository postRepository;

    @Test
    void shouldGetEmptyListOfCommentsByPostId() {
        // when
        when(this.commentRepository.getCommentsByPost_PostId(any(), any()))
                .thenReturn(new ArrayList<>());

        // then
        assertTrue(this.commentService.getAllCommentsByPostId(123L, null).isEmpty());
        verify(this.commentRepository).getCommentsByPost_PostId(any(), any());
    }

    @Test
    void shouldGetEmptyListOfAllComments() {
        // when
        when(this.commentRepository.findAllBy(any())).thenReturn(new ArrayList<>());

        // then
        assertTrue(this.commentService.getAllComments(null).isEmpty());
        verify(this.commentRepository).findAllBy(any());
    }
}

