package nz.westpac.user.controller;

import io.swagger.annotations.Api;
import nz.westpac.user.model.User;
import nz.westpac.user.model.Post;
import nz.westpac.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/v1", headers = "Accept=application/json", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "User operations")
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @GetMapping("/posts")
  ResponseEntity<List<Post>> getPosts() {
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(postService.getPosts());
  }

  @GetMapping("/posts/{id}/comments")
  ResponseEntity<List<User>> getCommentsByPostId(@PathVariable Integer id) {
  return ResponseEntity.ok(postService.getCommentsByPostId(id));
  }

  @GetMapping("/posts/{id}")
  public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
    return ResponseEntity.ok(postService.getPost(id));
  }
}
