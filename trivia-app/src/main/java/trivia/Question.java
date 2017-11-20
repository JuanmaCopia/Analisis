
package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

import org.json.JSONObject;
import org.json.JSONArray;

public class Question extends Model {

    //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
    static {
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

    /**
     * returns the id of the current object.
     * @pre. this != null
     * @return an int value that is the id of this question.
     * @post. the question id must be returned.
     */
    public int getQId() {
        return this.getInteger("id");
    }

    /**
     * returns the "pregunta" of the current question.
     * @pre. this != null
     * @return an String value that is the pregunta of this question.
     * @post. the question's pregunta must be returned.
     */
    public String getPregunta() {
        return this.getString("pregunta");
    }

    /**
     * returns the correctOption of the current question.
     * @pre. this != null
     * @return an int value that is the correctOption of this question.
     * @post. the question's correctOption must be returned.
     */
    public int getCorrectOption() {
        return this.getInteger("correctOption");
    }

    /**
     * Returns the category name the question belongs to.
     * @pre. this != null
     * @return an String value that is the category name this question belongs to.
     * @post. the category name this question belongs to, must be returned.
     */
    public String getCategoryName() {
        int category_id = this.getInteger("category_id");
        Category c = Category.findById(category_id);
        return c.getString("name");
    }

    /**
     * returns a not repeated random question.
     * @param gameId
     * @pre. this != null
     * @return a not repeated random question.
     * @post. a not repeated random question must be returned.
     */
    public static Question getRandomQuestion(int gameId) {
        List<Question> l = Question.findBySQL("select * from questions q where q.id not in (select gq.question_id from games_questions gq where game_id=" + gameId + ") order by rand() limit 1");
        return l.get(0);
    }

    /**
     * Returns a JSONObject contaiing all the data of the current Question object.
     * @pre. this != null
     * @return a JSONObject contaiing all the data of the current Question object.
     * @post a JSONObject contaiing all the data of the current Question object, must be returned.
    */
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        int category_id = this.getInteger("category_id");
        Category c = Category.findById(category_id);
        result.put("id",this.getInteger("id"));
        result.put("pregunta",this.getString("pregunta"));
        result.put("option1",this.getString("option1"));
        result.put("option2",this.getString("option2"));
        result.put("option3",this.getString("option3"));
        result.put("option4",this.getString("option4"));
        result.put("categoryName",c.getString("name"));
        return result;
    }

    /**
     * Returns 15 random questions for a match.
     * @pre. this != null
     * @return an String value that is the category name this question belongs to.
     * @post. the category name this question belongs to, must be returned.
     */
    public static JSONArray getMatchQuestions() {
        JSONArray questionsArray = new JSONArray();
        List<Question> questionsList = Question.findBySQL("SELECT DISTINCT * FROM questions ORDER BY RAND() LIMIT 10");
        for(Question q: questionsList){
            questionsArray.put(q.toJson());
        }
        return questionsArray;
    }

}
