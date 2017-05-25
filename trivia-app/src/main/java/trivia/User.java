package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide your password");
    validateWith(new UniquenessValidator("username")).message("This username is already taken."); // validar que sea unico
  }

  
}	