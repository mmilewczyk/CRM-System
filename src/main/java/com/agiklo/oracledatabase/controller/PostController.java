package com.agiklo.oracledatabase.controller;

import com.agiklo.oracledatabase.entity.discussions.Post;
import com.agiklo.oracledatabase.service.PostService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.agiklo.oracledatabase.controller.ApiMapping.POSTS_REST_URL;

@RestController
@RequestMapping(POSTS_REST_URL)
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Post postNewEmployee(@RequestBody Post post, Principal principal) {
        return postService.addNewPost(post, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable("id") Long id) throws NotFoundException {
        postService.deletePostById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(
            @PathVariable("id") Long id) {
        Optional<Post> optionalPosts = postService.getPostById(id);
        return optionalPosts
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public Set<Post> findPostsByAuthorFirstnameAndLastname(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName) throws NotFoundException {
        return postService.findPostsByAuthorFirstnameAndLastname(firstName, lastName);
    }
}
