package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

      User u = new User();
      u.set("username", "root");
      u.set("password", "root");
      u.saveIt();

      Base.close();
    }
}