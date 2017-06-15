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

    post("/sign_up_check", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        String username = request.queryParams("username");
        List<User> p = User.where("username = '" + username + "'");
        if (p.size()!=0){
            model.put("error", "El usuario " + username + " ya existe.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        String password = request.queryParams("password");
        String passwordRep = request.queryParams("passwordRep");
        if (!password.equals(passwordRep)){
            model.put("error", "Las contraseñas ingresadas no coinciden.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        String email = request.queryParams("email");
        p = User.where("email = '" + email + "'");
        if (p.size()!=0){
            model.put("error", "Ya existe un usuario registrado con este e-mail.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        User u = new User();
        u.setSignUpData(username, password, email);
        model.put("username",request.queryParams("username"));
        Base.close(); 
        return new ModelAndView(model, "./views/sign_up_check.mustache");
    }, new MustacheTemplateEngine());


    post("/game", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        // Chequeo de datos
        List<User> l = User.where("username = ? and password = ?",username,password);
        if (l.isEmpty()) {
        	model.put("error","Nombre de usuario o password invalido.");
        	Base.close();
        	return new ModelAndView(model,"./views/sign_in.mustache");
        }

        /// Creacion de sesion
        request.session(true);                     // create and return session
        request.session().attribute("user_id",l.get(0).getInteger("id")); // Set session attribute 'user'
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());

    post("/game2", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());

    post("/gameStart", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        // inicializo juego
        Game g = new Game();
        g.setBeginning(request.session().attribute("user_id"));
        request.session().attribute("game_id",g.getGId());
        // Busco pregunta aleatoria
        Question q = Question.getRandomQuestion(g.getGId());
        request.session().attribute("question_id",q.getQId());
        model.put("category_name", q.getCategoryName());
        model.put("question_name",q.getPregunta());
        model.put("option1",q.getString("option1"));
        model.put("option2",q.getString("option2"));
        model.put("option3",q.getString("option3"));
        model.put("option4",q.getString("option4"));
        Base.close(); 
        return new ModelAndView(model, "./views/gameAsk.mustache");
    }, new MustacheTemplateEngine());

    post("/answer", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        //Asigno puntaje
        int ansNum = Integer.parseInt(request.queryParams("user_answer"));
        int userId = request.session().attribute("user_id");
        int quesId = request.session().attribute("question_id");
        int gameId = request.session().attribute("game_id");
        Game.answerQuestion(quesId,userId,gameId,ansNum);
        // guardo pregunta en games questions
        GamesQuestions gq = new GamesQuestions();
        gq.setGameAndQuestionIds(gameId, quesId);
        ///
        Game g = Game.findById(gameId);
        if (g.getNumberQuestion()==9){
          g.setStateGameOver();
          model.put("rightAnswers", g.getRightAnswers());
          model.put("wrongAnswers", g.getWrongAnswers());
          Base.close(); 
          return new ModelAndView(model, "./views/gameFinished.mustache");
        }
        //Aumento contador de pregunta
        g.increaseNumberQuestion();
        // Busco otra pregunta aleatoria
        Question q = Question.getRandomQuestion(gameId);
        request.session().attribute("question_id",q.getQId()); // set questid
        model.put("category_name", q.getCategoryName());
        model.put("question_name",q.getPregunta());
        model.put("option1",q.getString("option1"));
        model.put("option2",q.getString("option2"));
        model.put("option3",q.getString("option3"));
        model.put("option4",q.getString("option4"));
        Base.close(); 
        return new ModelAndView(model, "./views/gameAsk.mustache");
    }, new MustacheTemplateEngine());



    get("/ranking", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        List<User> ranking = User.findBySQL("SELECT * FROM users ORDER BY rightAnswers DESC LIMIT 10");
        User u = ranking.get(0); // eliminar
        model.put("top10",ranking);
        Base.close(); 
        return new ModelAndView(model, "./views/rankingView.mustache");
    }, new MustacheTemplateEngine());


    post("/logOut", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        // destruyo sesion
        if (request.session().attribute("user_id") != null) 
        	request.session().removeAttribute("user_id");
        if (request.session().attribute("game_id") != null) 
        	request.session().removeAttribute("game_id");
        if (request.session().attribute("question_id") != null) 
        	request.session().removeAttribute("question_id");
        Base.close(); 
        return new ModelAndView(model, "./views/index.mustache");
    }, new MustacheTemplateEngine());
 
    }
}