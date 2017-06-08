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
        return new ModelAndView(model, "./src/main/resources/templates/ppal.mustache"); 
    }, new MustacheTemplateEngine());

    post("/registrarse", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./src/main/resources/templates/registro.mustache");
    },new MustacheTemplateEngine());

    post("/ingresar", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./src/main/resources/templates/ingreso.mustache");
    },new MustacheTemplateEngine());





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


    }
}