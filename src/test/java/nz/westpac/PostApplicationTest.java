package nz.westpac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PostApplication.class, properties = {"server.port=9605",
}, webEnvironment = RANDOM_PORT)
@ActiveProfiles({"test", "local"})
@DirtiesContext
public class PostApplicationTest {

  @Test
  public void contextLoads() {
  }

  @Test
  public void test() {
    PostApplication.main(new String[]{
        "--spring.main.web-environment=false",
        "--spring.autoconfigure.exclude="});
  }
}
