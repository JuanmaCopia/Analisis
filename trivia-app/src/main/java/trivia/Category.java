package trivia;

import org.javalite.activejdbc.Model;
import java.util.Random;

public class Category extends Model {
	static{
    validatePresenceOf("name").message("Please, provide your category name");
  }
}