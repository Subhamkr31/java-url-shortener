import io.lettuce.core.*;
public class T {
  public static void main(String[] a) {
    RedisURI u = RedisURI.create(""redis://localhost:6379"");
    System.out.println(""n oauth: "" + u.getCredentialsProvider());
    u = RedisURI.create(""redis://user:secret@localhost:6379"");
    System.out.println(""auth: "" + u.getCredentialsProvider());
    if (u.getCredentialsProvider() != null) {
      RedisCredentials c = u.getCredentialsProvider().resolveCredentials().block();
      System.out.println(c.getUsername() + "" / "" + new String(c.getPassword()));
    }
  }
}
