package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model {

	static{
    	validatePresenceOf("user_id").message("Please, provide the user id");
    	validatePresenceOf("state").message("Please, provide the state of the game.");
  	}

  	public int getGId(){
		return this.getInteger("id");
	}

	public Integer getNumberQuestion(){
		return (Integer)this.get("cantPreg");
	}

	public Integer getRightAnswers(){
		return this.getInteger("rightAnswers");
	}

	public Integer getWrongAnswers(){
		return this.getInteger("wrongAnswers");
	}

	public String getState(){
		return this.getString("state");
	}

	public void setBeginning(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.set("rightAnswers", 0);
    	this.set("wrongAnswers", 0);
    	this.saveIt();
	}

	public void setStateGameOver(){
		this.set("state","Game_Over");
		this.saveIt();
	}

	public void increaseNumberQuestion(){
		this.set("cantPreg",(Integer)this.get("cantPreg")+1);
        this.saveIt();
	}

	public static void answerQuestion(int question_id, int user_id,int game_id, int option) {
		Question q = Question.findById(question_id);		
		int correctAnswer = q.getCorrectOption();
		Game g = Game.findById(game_id);
		User u = User.findById(user_id);
		if (option == correctAnswer) {
			u.increaseScore();
			g.set("rightAnswers",g.getInteger("rightAnswers") + 1); // rightAnswers 
			g.saveIt();
		}
		else {
			u.decreaseScore();
			g.set("wrongAnswers",g.getInteger("wrongAnswers") + 1); // wrongAnswers
			g.saveIt();
		}

	}

}