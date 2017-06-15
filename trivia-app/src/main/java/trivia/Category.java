package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class Category extends Model {
	static{
    	validatePresenceOf("name").message("Please, provide your category name");
    	validateWith(new UniquenessValidator("name")).message("This name is already taken.");
  	}
  	public void setCategoryName(String name){
  		this.set("name",name);
  		this.saveIt();
  	}

}