package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Question extends Model {

  	public static Question getRandomQuestion(){
    	Random r = new Random();
    	int i = r.nextInt((Question.count()).intValue()-2);
    	Question q = Question.findById(i);
    	return q;
  	}

  	public static String getCorrectAwnser(Question q) {
		String resp = q.getString("correctOption");
		return resp;
 	}

 	public static boolean esCorrecta(Question q, String resp) {
		return (resp == q.getCorrectAwnser(q));
	}
}