package nz.westpac.user.service;

import nz.westpac.user.model.User;
import nz.westpac.user.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Value("${get.users.endpoint}")
  private String getUsersEndpoint;

  @Value("${get.usersById.endpoint}")
  private String getUsersByIdEndpoint;

  @Value("${get.postsByUser.get.endpoint}")
  private String postsByUserEndpoint;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public Post getPost(Integer id){
        return restTemplate.getForObject(getUsersByIdEndpoint,Post.class,id);
   }

  @Override
  public List<User> getCommentsByPostId(Integer postId){
    Map<String, Integer> uriParams = new HashMap<String, Integer>();
    uriParams.put("id",postId);
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getComentByPostIdEndpoint);
    logger.debug("Request URI {}", builder.buildAndExpand(uriParams).toUri());
    URI uri= builder.buildAndExpand(uriParams).toUri();
    ResponseEntity<List<User>> response=restTemplate.exchange(uri, HttpMethod.GET,null, new ParameterizedTypeReference<List<User>>() {});
    return response.getBody();
  }

  @Override
  public List<Post> getPosts(){
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(postsGetEndpoint);
    ResponseEntity<List<Post>> response=restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,null, new ParameterizedTypeReference<List<Post>>() {});
    List<Post> posts=response.getBody();
    return posts;
  }




}
