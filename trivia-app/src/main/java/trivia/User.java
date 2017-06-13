package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide your password");
    validateWith(new UniquenessValidator("username")).message("This username is already taken."); // validar que sea unico
  }

/*
  public Game playGame(){
  	int id = (Long)this.get("id");
  	Game g = new Game(id);
  	return g;
  }*/



  public String username () {
    return (String) this.get("username");
  }

  public Integer getId(){
    return this.getInteger("id");
  }

/*
  public String responderBien (Question q) {
	return Question.getCorrectAnswer(q);
  } 

  public String responderMal(Question q){
  	return Question.getWrongAnswer(q);
  }*/

  public void setSignUpData(String username, String password, String email){
    this.set("username",username);
    this.set("password",password);
    this.set("email",email);
    this.set("rightAnswers",0);
    this.set("wrongAnswers",0);
    this.saveIt();
  }

  public void increaseScore(){
  	this.set("rightAnswers",this.getInteger("rightAnswers")+1);
	  this.saveIt();
  }

  public void decreaseScore(){
  	this.set("wrongAnswers",this.getInteger("wrongAnswers")+1);
	  this.saveIt();
  }
  
}	