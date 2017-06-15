package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class User extends Model {

  //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
  static{
    validatePresenceOf("username").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide your password");
    validatePresenceOf("email").message("Please, provide your email");
    validateEmailOf("email");
    validateWith(new UniquenessValidator("username")).message("This username is already taken.");
    validateWith(new UniquenessValidator("email")).message("This email is already taken.");
  }

  //Retorna el id de usuario.
  public Integer getUId(){
    return this.getInteger("id");
  }

  //Retorna la cantidad de respuestas correctas que ha acumulado el usuario.
  public int rightAnswers() {
    return this.getInteger("rightAnswers");
  }

  //Retorna el nombre de usuario.
  public String getUsername() {
    return (String) this.get("username");
  }

  //Setea los datos del usuario cuando se registra en el juego como nuevo usuario.
  public void setSignUpData(String username, String password, String email){
    this.set("username",username);
    this.set("password",password);
    this.set("email",email);
    this.set("rightAnswers",0);
    this.set("wrongAnswers",0);
    this.saveIt();
  }

  //Incrementa el puntaje del usuario.
  public void increaseScore(){
  	this.set("rightAnswers",this.getInteger("rightAnswers")+1);
	  this.saveIt();
  }

  //Decrementa el puntaje del usuario.
  public void decreaseScore(){
  	this.set("wrongAnswers",this.getInteger("wrongAnswers")+1);
	  this.saveIt();
  }
  
}	