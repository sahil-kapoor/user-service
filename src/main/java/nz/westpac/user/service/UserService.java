package nz.westpac.user.service;

import nz.westpac.user.model.User;
import nz.westpac.user.model.Post;

import java.util.List;

public interface UserService {

 public Post getPost(Integer postId);
 public List<User> getCommentsByPostId(Integer postId);
 public List<Post> getPosts();

}
