package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Question extends Model {

	public static int getRandomQuestion(){
  	Random r = new Random();
   	return r.nextInt((Question.count()).intValue() - 1) + 1;
   	//Question q = Question.findById(i);
   	//return q;
  }

  public String getPregunta(){
    return this.getString("pregunta");
  }

  public static String getWrongAnswer(Question q){
    String resp = q.getString("option1");
    return resp;
  }
/*
  public static String getCorrectAnswer(Question q) {
	  String resp = q.getString("correctOption");
	  return resp;
 	}

 	public static boolean esCorrecta(Question q, String resp) {
		return (resp == q.getCorrectAnswer(q));
	}*/

  public static String getCategoryName(Question q){
    int category_id = q.getInteger("category_id");
    Category c = Category.findById(category_id);
    return c.getString("name");
  }
}