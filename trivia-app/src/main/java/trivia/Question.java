package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Question extends Model {

  static{
    validatePresenceOf("pregunta").message("Please, provide the question");
    validatePresenceOf("option1").message("Please, provide the option1");
    validatePresenceOf("option2").message("Please, provide the option2");
    validatePresenceOf("option3").message("Please, provide the option3");
    validatePresenceOf("option4").message("Please, provide the option4");
    validatePresenceOf("correctOption").message("Please, provide the correct option");
    validatePresenceOf("category_id").message("Please, provide the category id");
    validateRange("correctOption", 1, 4).message("correctOption.outside.limits");
    //int max = (Category.count()).intValue();
    //validateRange("category_id", 1, max).message("category_id.outside.limits");
    validateWith(new UniquenessValidator("pregunta")).message("This question exists already.");
  }

  public int getQId(){
    return this.getInteger("id");
  }

	public static int getRandomQuestion(){
  	Random r = new Random();
   	return r.nextInt((Question.count()).intValue() - 1) + 1;
  }

  public String getPregunta(){
    return this.getString("pregunta");
  }

  public int getWrongAnswer(){
    int resp = this.getInteger("correctOption");
    if (resp<4){
      resp = resp++;
    }
    else{
      resp = resp--;
    }
    return resp;
  }

  public int getCorrectOption() {
	  int resp = this.getInteger("correctOption");
	  return resp;
 	}

  public String getCategoryName(){
    int category_id = this.getInteger("category_id");
    Category c = Category.findById(category_id);
    return c.getString("name");
  }
}