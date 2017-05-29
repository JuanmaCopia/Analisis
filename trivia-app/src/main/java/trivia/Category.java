package trivia;

import org.javalite.activejdbc.Model;
import java.util.Random;

public class Category extends Model {
	static{
    validatePresenceOf("name").message("Please, provide your category name");
  }

	public static String getCategoryName(Question q){
		int category_id = q.getInteger("category_id");
		Category c = Category.findById(category_id);
		return c.getString("name");
	}
}