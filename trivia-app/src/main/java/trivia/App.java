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

      /*User u = new User();
      u.set("username", "root");
      u.set("password", "root");
      u.saveIt();
*/
      User s = new User();
      s.set("username", "asd");
      s.set("password", "juanma");
      s.set("score",0);
      s.saveIt();

      int sebaId=s.getInteger("id");
      Game g = new Game(sebaId);

      Base.close();
    }
}