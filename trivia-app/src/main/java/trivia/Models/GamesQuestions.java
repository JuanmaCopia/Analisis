
package trivia;

import org.javalite.activejdbc.Model;

public class GamesQuestions extends Model {

	/**
     * sets the game id and the question id of the "this" GamesQuestion object and save
     * the change into the database.
     * @pre. this != null
     * @param gameId is the id of the game.
     * @param quesId is the id of the question.
     * @post. game_id must be equal to gameId and question_id must be equal to quesId.
     */
	public void setGameAndQuestionIds(int gameId, int quesId) {
        this.set("game_id",gameId);
        this.set("question_id",quesId);
        this.saveIt();
	}
}
