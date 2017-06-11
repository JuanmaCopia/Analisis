package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model {
	public Game(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.set("rightAnswers", 0);
    	this.set("wrongAnswers", 0);
    	this.saveIt();
	}

	public void increaseNumberQuestion(){
		this.set("cantPreg",(Integer)this.get("cantPreg")+1);
        this.saveIt();
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

/*
public Game(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.saveIt();
		User usuario = User.findById(user_id);
		while((Integer)this.get("cantPreg")<2) {
			// Busco pregunta aleatoria
			int quesId = Question.getRandomQuestion();
			// Me fijo que categoria tiene y la muestro
			Question q = Question.findById(quesId);
			System.out.println("Categoria: "+ Category.getCategoryName(q));
			// Le pregunto al usuario
			System.out.println(q.getPregunta());
			String respuesta = usuario.responderBien(q);
			//String respuesta = usuario.responderMal(q);
			System.out.println("Respondio: "+respuesta);
			if (Question.esCorrecta(q,respuesta)) {
				System.out.println("La respuesta es correcta");
				usuario.incrementScore();
			}
			else {
				System.out.println("La respuesta es incorrecta");
				usuario.decrementScore();
			}
			this.set("cantPreg",(Integer)this.get("cantPreg")+1);
			this.saveIt();
		}
		if((Integer)this.get("cantPreg")>=2){
        	this.set("state","Game_Over");
    		this.saveIt();
    	}

	}
*/

}