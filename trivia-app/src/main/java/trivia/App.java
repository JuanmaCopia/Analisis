package trivia;

import org.javalite.activejdbc.Base;

import trivia.User;

import java.util.*;

import static spark.Spark.*;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;



public class App {

    public static void main( String[] args ) {

    staticFileLocation("/public");

   
    get("/", (request, response) -> {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "./views/index.mustache"); 
    }, new MustacheTemplateEngine());

    get("/users", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        List<User> lista = User.findAll();
        Map model = new HashMap();
        lista.get(0); // eliminar
        model.put("users",lista);
        Base.close(); 
        return new ModelAndView(model, "./views/users.mustache");
    }, new MustacheTemplateEngine());




    get("/sign_up", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./views/sign_up.mustache");
    },new MustacheTemplateEngine());

    get("/sign_in", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./views/sign_in.mustache");
    },new MustacheTemplateEngine());

    post("/sign_up_success", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

        Map model = new HashMap();
        model.put("username",request.queryParams("username"));
        model.put("password",request.queryParams("password"));

        Base.close(); 
        return new ModelAndView(model, "./views/sign_up_success.mustache");
    }, new MustacheTemplateEngine());


    post("/game", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        request.session(true);                     // create and return session
        String userN = request.queryParams("username");
        List<User> p = User.where("username = '" + userN + "'");
        request.session().attribute("user_id",p.get(0).getInteger("id")); // Set session attribute 'user'
        Map model = new HashMap();
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());

    post("/playNewGameBeginning", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        // inicializo juego
        Game g = new Game(request.session().attribute("user"));
        request.session().attribute("game_id",g.getInteger("id"));
        // Busco pregunta aleatoria
        Question q = Question.findById(Question.getRandomQuestion());
        request.session().attribute("question_id",q.getInteger("id"));
        model.put("category_name", Category.getCategoryName(q));
        model.put("question_name",q.getString("pregunta"));
        model.put("option1",q.getString("option1"));
        model.put("option2",q.getString("option2"));
        model.put("option3",q.getString("option3"));
        model.put("option4",q.getString("option4"));
        Base.close(); 
        return new ModelAndView(model, "./views/playNewGameBeginning.mustache");
    }, new MustacheTemplateEngine());

    post("/playNewGameMiddle", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        //Asigno puntaje
        Integer ansNum = Integer.parseInt(request.queryParams("answerNumber"));
        Integer userId = request.session().attribute("user_id");
        Integer quesId = request.session().attribute("question_id");
        Integer gameId = request.session().attribute("game_id");
        answerQuestion(quesId,userId,gameId,ansNum);
        Game g = Game.findById(gameId);
        if (g.getNumberQuestion()=5){
          model.put("rightAnswers", g.getRightAnswers());
          model.put("wrongAnswers", g.getWrongAnswers());
          Base.close(); 
          return new ModelAndView(model, "./views/playNewGameEnd.mustache");
        }
        //Aumento contador de pregunta
        g.incrementNumberQuestion();
        // Busco otra pregunta aleatoria
        Question q = Question.findById(Question.getRandomQuestion());
        request.session().attribute("question_id",q.getInteger("id"));
        model.put("category_name", Category.getCategoryName(q));
        model.put("question_name",q.getString("pregunta"));
        model.put("option1",q.getString("option1"));
        model.put("option2",q.getString("option2"));
        model.put("option3",q.getString("option3"));
        model.put("option4",q.getString("option4"));
        Base.close(); 
        return new ModelAndView(model, "./views/playNewGameMiddle.mustache");
    }, new MustacheTemplateEngine());
/*
    post("/playNewGameEnd", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        Game g = Game.findById(request.session().attribute("game_id"));
        model.put("rightAnswers", g.getRightAnswers());
        model.put("wrongAnswers", g.getWrongAnswers());
        Base.close(); 
        return new ModelAndView(model, "./views/playNewGameEnd.mustache");
    }, new MustacheTemplateEngine());

*/


    /*
	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");

      /*User u = new User();
      u.set("username", "root");
      u.set("password", "root");
      u.saveIt();

      User s = new User();
      s.set("username", "asdaaaahaassssjj");
      s.set("password", "juanma");
      s.set("rightAnswers",0);
      s.set("wrongAnswers",0);
      s.saveIt();

      int sebaId=s.getInteger("id");
      Game g = new Game(sebaId);
      //Game g = s.playGame();

      Base.close();
	*/

      /*
      request.session(true);                     // create and return session
request.session().attribute("user");       // Get session attribute 'user'
request.session().attribute("user","foo"); // Set session attribute 'user'
request.session().removeAttribute("user");
*/

    }
}