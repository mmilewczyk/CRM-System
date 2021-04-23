package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.PostDTO;
import com.agiklo.oracledatabase.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.agiklo.oracledatabase.controller.ApiMapping.POSTS_REST_URL;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(POSTS_REST_URL)
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllPosts(Pageable pageable){
        return status(HttpStatus.OK).body(postService.getAllPosts(pageable));
    }

    @PostMapping(consumes="application/json")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<Post> addNewPost(@RequestBody Post post, Principal principal) {
        return status(HttpStatus.CREATED).body(postService.addNewPost(post, principal));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public void deletePostById(@PathVariable("id") Long id, Principal principal) {
        postService.deletePostById(id, principal);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<PostDTO> editPostContent(@RequestBody Post post, Principal principal){
        return status(HttpStatus.OK).body(postService.editPostContent(post, principal));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id) {
        return status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<Set<PostDTO>> getPostsByAuthorFirstnameAndLastname(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            Pageable pageable){
        return status(HttpStatus.OK).body(postService.findPostsByAuthorFirstnameAndLastname(firstName, lastName, pageable));
    }
}
