package nz.westpac.user.controller;

import io.swagger.annotations.Api;
import nz.westpac.user.model.Post;
import nz.westpac.user.model.User;
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

  @GetMapping("/users/{userId}")
  ResponseEntity<User> getUserById(@PathVariable Integer userId) {
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(userService.getUser(userId));
  }

  @GetMapping("/users")
  ResponseEntity<List<User>> getUsers() {
  return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping("/users/{userId}/post")
  public ResponseEntity<List<Post>> getPostByUserId(@PathVariable Integer userId) {
    return ResponseEntity.ok(userService.getPostsByUserId(userId));
  }
}
