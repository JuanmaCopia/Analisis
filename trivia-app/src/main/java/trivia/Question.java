package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Question extends Model {

  //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
  static{
    validatePresenceOf("pregunta").message("Please, provide the question");
    validatePresenceOf("option1").message("Please, provide the option1");
    validatePresenceOf("option2").message("Please, provide the option2");
    validatePresenceOf("option3").message("Please, provide the option3");
    validatePresenceOf("option4").message("Please, provide the option4");
    validatePresenceOf("correctOption").message("Please, provide the correct option");
    validatePresenceOf("category_id").message("Please, provide the category id");
    validateRange("correctOption", 1, 4).message("correctOption.outside.limits");
    validateWith(new UniquenessValidator("pregunta")).message("This question exists already.");
  }

  //Retorna el id de Question.
  public int getQId(){
    return this.getInteger("id");
  }

  //Retorna la pregunta.
  public String getPregunta(){
    return this.getString("pregunta");
  }

  //Retorna la opcion correcta.
  public int getCorrectOption() {
    int resp = this.getInteger("correctOption");
    return resp;
  }

  //Retorna el nombre de la categoria que corresponda a la pregunta con la cual se invoca el metodo.
  public String getCategoryName(){
    int category_id = this.getInteger("category_id");
    Category c = Category.findById(category_id);
    return c.getString("name");
  }

  //Retorna una pregunta de manera aleatoria.
	public static Question getRandomQuestion(int gameId){
    List<Question> l = Question.findBySQL("select * from questions q where q.id not in (select gq.question_id from games_questions gq where game_id=" + gameId + ") order by rand() limit 1");
    return l.get(0);
  }
}