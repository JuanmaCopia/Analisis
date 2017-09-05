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
   
   // Retorna vista Principal
    get("/", (request, response) -> {
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(model, "./views/index.mustache"); 
    }, new MustacheTemplateEngine());

    // Retorna vista de registro
    get("/sign_up", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./views/sign_up.mustache");
    },new MustacheTemplateEngine());

    // Retorna vista de ingreso
    get("/sign_in", (req,res) -> {
      Map<String, Object> model = new HashMap();
      return new ModelAndView(model, "./views/sign_in.mustache");
    },new MustacheTemplateEngine());

    // Chequeo de datos de registro, si son correctos se retorna la vista de bienvenida, sino se informa el error sobre la vista de regitro
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

    // Creacion de la sesion y retorno de VISTA DE MENU DE JUEGO en el caso que los datos de ingreso sean correctos, sino se informa el error sobre la vista de ingreso 
    post("/game", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        List<User> l = User.where("username = ? and password = ?",username,password);
        if (l.isEmpty()) {
        	model.put("error","Nombre de usuario o contraseña invalido/s.");
        	Base.close();
        	return new ModelAndView(model,"./views/sign_in.mustache");
        }
        request.session(true);                                            
        request.session().attribute("user_id",l.get(0).getInteger("id")); 
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());

    // Retorna vista de MENU DE JUEGO ALTERNATIVA (En esta no se crea la sesion porque se ejecuta cuando ya esta creada) 
    get("/game2", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        Base.close();
        return new ModelAndView(model, "./views/game.mustache");
    }, new MustacheTemplateEngine());

    // Comienzo de juego: busca pregunta aleatoria y retorna la vista de juego que muestra la pregunta
    post("/gameStart", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        Game g = new Game();
        g.setBeginning(request.session().attribute("user_id"));      
        request.session().attribute("game_id",g.getGId()); 
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

    // Chequea si la respuesta es correcta o no, e incrementa los contadores correspondientes, se engarga tambien de indicar si la respuesta fue correcta o no, y en caso de ser incorrecta decir cual deberia haber sido.
    post("/answer", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        int ansNum = Integer.parseInt(request.queryParams("user_answer")); 
        int userId = request.session().attribute("user_id");       
        int quesId = request.session().attribute("question_id");           
        int gameId = request.session().attribute("game_id");
        Game.answerQuestion(quesId,userId,gameId,ansNum);
        GamesQuestions gq = new GamesQuestions();
        gq.setGameAndQuestionIds(gameId, quesId);
        Question q = Question.findById(quesId);
        int correctOption = q.getCorrectOption();
        if (correctOption == ansNum){
            Base.close(); 
            return new ModelAndView(model, "./views/rightAnswer.mustache");
        }
        else{
            switch (correctOption){
                case 1: model.put("correctOp", q.getString("option1"));
                        break;
                case 2: model.put("correctOp", q.getString("option2"));
                        break;
                case 3: model.put("correctOp", q.getString("option3"));
                        break;
                case 4: model.put("correctOp", q.getString("option4"));
                        break;
            }
            Base.close(); 
            return new ModelAndView(model, "./views/wrongAnswer.mustache");
        }
    }, new MustacheTemplateEngine());

    //Se engarga de buscar la siguiente pregunta e insertarla en el modelo, o mostrar el puntaje final en caso de haber finalizado la partida.
    post("/nextQuestion", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();          
        int gameId = request.session().attribute("game_id");
        Game g = Game.findById(gameId);
        g.increaseNumberQuestion();
        if (g.getNumberQuestion()==10){
          g.setStateGameOver(); 
          model.put("rightAnswers", g.getRightAnswers());
          model.put("wrongAnswers", g.getWrongAnswers());
          Base.close(); 
          return new ModelAndView(model, "./views/gameFinished.mustache");
        }
        Question q = Question.getRandomQuestion(gameId);
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

    // Retorna la vista del ranking (Top 10)
    get("/ranking", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        List<User> ranking = User.findBySQL("SELECT * FROM users ORDER BY rightAnswers DESC LIMIT 10");
        User u = ranking.get(0);
        model.put("top10",ranking);
        Base.close(); 
        return new ModelAndView(model, "./views/rankingView.mustache");
    }, new MustacheTemplateEngine());

    // Cierre de sesion: Se destruye la session y se retorna el menu principal de la pagina
    post("/logOut", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
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