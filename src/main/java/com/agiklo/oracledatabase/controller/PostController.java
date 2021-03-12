package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.Post;
import com.agiklo.oracledatabase.entity.dto.PostDTO;
import com.agiklo.oracledatabase.service.PostService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Post addNewPost(@RequestBody Post post, Principal principal) {
        return postService.addNewPost(post, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable("id") Long id) throws NotFoundException {
        postService.deletePostById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id) {
        return status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Set<PostDTO>> findPostsByAuthorFirstnameAndLastname(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName){
        return status(HttpStatus.OK).body(
                postService.findPostsByAuthorFirstnameAndLastname(firstName, lastName));
    }
}
