package trivia;
import org.javalite.activejdbc.Model;

public class GamesQuestions extends Model {

	public void setGameAndQuestionIds(int gameId, int quesId){
        this.set("game_id",gameId);
        this.set("question_id",quesId);
        this.saveIt();
	}
}