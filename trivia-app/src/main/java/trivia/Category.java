package trivia;

import org.javalite.activejdbc.Model;

public class Category extends Model {
	static{
    validatePresenceOf("category_name").message("Please, provide your category name");
  }
}