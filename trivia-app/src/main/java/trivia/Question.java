package trivia;

import org.javalite.activejdbc.Model;

public class Question extends Model {

  	public Question getRandomQuestion(){
    	Random r = new Random();
    	int i = r.nextInt(Question.count());
    	Question q = Question.findById(i);
    	return q;
  	}
}