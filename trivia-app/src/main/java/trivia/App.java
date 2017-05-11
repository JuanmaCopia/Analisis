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
      	u.set("Nicolas", "root");
      	u.set("Fraschetti", "root");
      	u.saveIt();

      	User v=new User();
	  	u.set("Juan Manuel", "root");
      	u.set("Copia", "root");
      	u.saveIt();

	  	User w=new User();
	  	u.set("Sebastian", "root");
      	u.set("Fischer", "root");
      	u.saveIt();      

		Base.close();
    }
}