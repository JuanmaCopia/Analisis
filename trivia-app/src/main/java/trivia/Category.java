package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class Category extends Model {
	//Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
	static{
    	validatePresenceOf("name").message("Please, provide your category name");
    	validateWith(new UniquenessValidator("name")).message("This name is already taken.");
  	}

  	//Setea el nombre de la categoria.
  	public void setCategoryName(String name){
  		this.set("name",name);
  		this.saveIt();
  	}

}