
package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Category extends Model {

	//Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
	static {
        validatePresenceOf("name").message("Please, provide your category name");
    	validateWith(new UniquenessValidator("name")).message("This name is already taken.");
  	}

  	/**
     * sets the category name and save the change into the database.
     * @pre. this != null
     * @param. name is the new name for the category.
     * @post. category name must be equal to name.
     */
  	public void setCategoryName(String name) {
  		this.set("name",name);
  		this.saveIt();
  	}

}
