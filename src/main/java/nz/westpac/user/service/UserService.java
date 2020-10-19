package nz.westpac.user.service;

import nz.westpac.user.model.Post;
import nz.westpac.user.model.User;

import java.util.List;

public interface UserService {

 public User getUser(Integer userId);
 public List<Post> getPostsByUserId(Integer userId);
 public List<User> getUsers();

}
