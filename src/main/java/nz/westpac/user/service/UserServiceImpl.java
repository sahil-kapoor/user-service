package nz.westpac.user.service;

import nz.westpac.user.model.Post;
import nz.westpac.user.model.User;
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

  @Value("${get.postsByUser.endpoint}")
  private String postsByUserEndpoint;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public User getUser(Integer id){
        return restTemplate.getForObject(getUsersByIdEndpoint,User.class,id);
   }

  @Override
  public List<Post> getPostsByUserId(Integer userId){
    Map<String, Integer> uriParams = new HashMap<String, Integer>();
    uriParams.put("id",userId);
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUsersByIdEndpoint);
    logger.debug("Request URI {}", builder.buildAndExpand(uriParams).toUri());
    URI uri= builder.buildAndExpand(uriParams).toUri();
    ResponseEntity<List<Post>> response=restTemplate.exchange(uri, HttpMethod.GET,null, new ParameterizedTypeReference<List<Post>>() {});
    return response.getBody();
  }

  @Override
  public List<User> getUsers(){
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUsersEndpoint);
    ResponseEntity<List<User>> response=restTemplate.exchange(builder.build().toUri(), HttpMethod.GET,null, new ParameterizedTypeReference<List<User>>() {});
    List<User> posts=response.getBody();
    return posts;
  }




}
