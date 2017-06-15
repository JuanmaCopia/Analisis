package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model {

	//Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
	static{
    	validatePresenceOf("user_id").message("Please, provide the user id");
    	validatePresenceOf("state").message("Please, provide the state of the game.");
  	}

  	//Retorna el Id de juego.
  	public int getGId(){
		return this.getInteger("id");
	}

	//Retorna la cantidad de preguntas actual.
	public Integer getNumberQuestion(){
		return (Integer)this.get("cantPreg");
	}

	//Retorna la cantidad de respuestas correctas.
	public Integer getRightAnswers(){
		return this.getInteger("rightAnswers");
	}

	//Retorna la antidad de respuestas incorrectas.
	public Integer getWrongAnswers(){
		return this.getInteger("wrongAnswers");
	}

	//Retorna el estado del juego.
	public String getState(){
		return this.getString("state");
	}

	//Setea todos los campos que corresponden al inicio de un juego.
	public void setBeginning(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.set("rightAnswers", 0);
    	this.set("wrongAnswers", 0);
    	this.saveIt();
	}

	//Setea el estado de un juego en Game Over (para juego finalizado).
	public void setStateGameOver(){
		this.set("state","Game_Over");
		this.saveIt();
	}

	//Incrementa en uno el contador de preguntas del juego.
	public void increaseNumberQuestion(){
		this.set("cantPreg",(Integer)this.get("cantPreg")+1);
        this.saveIt();
	}

	//Se encarga de chequear si la opcion elegida por el usuario como respuesta a una pregunta es correcta o
	//incorrecta, y asigna el puntaje correspondiente para Game y User.
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