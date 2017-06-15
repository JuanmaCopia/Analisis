package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator; 

public class User extends Model {
  static{
    validatePresenceOf("username").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide your password");
    validatePresenceOf("email").message("Please, provide your email");
    validateEmailOf("email");
    validateWith(new UniquenessValidator("username")).message("This username is already taken.");
    validateWith(new UniquenessValidator("email")).message("This email is already taken.");
  }


  public int rightAnswers() {
    return this.getInteger("rightAnswers");
  }

  public String getUsername() {
    return (String) this.get("username");
  }

  public Integer getUId(){
    return this.getInteger("id");
  }


  public int answerOk(Question q) {
  	return q.getCorrectOption();
  } 

  public int answerNotOk(Question q){
  	return q.getWrongAnswer();
  }

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