package nz.westpac.exception;

public class PostNotFoundException extends RuntimeException {

    PostNotFoundException(Integer id) {
        super("Could not find user " + id);
    }

}
