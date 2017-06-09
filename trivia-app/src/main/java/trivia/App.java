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
        request.session().attribute("user",p.get(0).getInteger("id")); // Set session attribute 'user'
        Map model = new HashMap();
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());



    post("/playNewGame", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();

        Base.close(); 
        return new ModelAndView(model, "./views/playNewGame.mustache");
    }, new MustacheTemplateEngine());




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