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
        // recuperacion de nombre de usuario
        String username = request.queryParams("username");
        // chequeo de existencia
        List<User> p = User.where("username = '" + username + "'");
        if (p.size()!=0){
            model.put("error", "El usuario " + username + " ya existe.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        // recuperacion de contraseñas
        String password = request.queryParams("password");
        String passwordRep = request.queryParams("passwordRep");
        // chequeo de igualdad
        if (!password.equals(passwordRep)){
            model.put("error", "Las contraseñas ingresadas no coinciden.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        // recuperacion de email
        String email = request.queryParams("email");
        // chequeo de existencia
        p = User.where("email = '" + email + "'");
        if (p.size()!=0){
            model.put("error", "Ya existe un usuario registrado con este e-mail.");
            Base.close(); 
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        // registro de usuario en la base de datos
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
        // Recuperacion de datos de ingreso
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        // Chequeo de datos
        List<User> l = User.where("username = ? and password = ?",username,password);
        if (l.isEmpty()) {
        	model.put("error","Nombre de usuario o contraseña invalido/s.");
        	Base.close();
        	return new ModelAndView(model,"./views/sign_in.mustache");
        }
        /// Creacion de sesion
        request.session(true);                                            // crea y retorna la sesion
        request.session().attribute("user_id",l.get(0).getInteger("id")); // set del atributo 'user_id' en la sesion
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
        // Inicializacion del juego
        Game g = new Game();
        g.setBeginning(request.session().attribute("user_id"));      
        request.session().attribute("game_id",g.getGId()); // set del id de juego en la session    
        Question q = Question.getRandomQuestion(g.getGId());  // busqueda de pregunta aleatoria  
        request.session().attribute("question_id",q.getQId()); // set del id de pregunta en la sesion
        // Insercion de datos de la pregunta en el modelo
        model.put("category_name", q.getCategoryName());
        model.put("question_name",q.getPregunta());
        model.put("option1",q.getString("option1"));
        model.put("option2",q.getString("option2"));
        model.put("option3",q.getString("option3"));
        model.put("option4",q.getString("option4"));
        Base.close(); 
        return new ModelAndView(model, "./views/gameAsk.mustache");
    }, new MustacheTemplateEngine());

    // Chequea si la respuesta es correcta o no, e incrementa los contadores correspondientes, se engarga tambien de buscar la siguiente pregunta y insertarla en el modelo
    post("/answer", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        // Recuperacion de datos
        int ansNum = Integer.parseInt(request.queryParams("user_answer")); // respuesta del usuario
        int userId = request.session().attribute("user_id");       
        int quesId = request.session().attribute("question_id");           
        int gameId = request.session().attribute("game_id");
        // Se responde la pregunta e incrementan contadores de respuesta correcta / incorrecta
        Game.answerQuestion(quesId,userId,gameId,ansNum);
        // Se registra que la pregunta ya fue pregutnada en este juego
        GamesQuestions gq = new GamesQuestions();
        gq.setGameAndQuestionIds(gameId, quesId);
        // Si es la ultima pregunta se retorna la vista de finalizacion de juego
        Game g = Game.findById(gameId);
        if (g.getNumberQuestion()==9){
          g.setStateGameOver(); // cambio de estado a game over
          // Se insertan la cantidad de preguntas correctas e incorrectas en el modelo
          model.put("rightAnswers", g.getRightAnswers());
          model.put("wrongAnswers", g.getWrongAnswers());
          Base.close(); 
          return new ModelAndView(model, "./views/gameFinished.mustache");
        }
        // Aumento del contador de pregunta
        g.increaseNumberQuestion();
        // Busqueda de otra pregunta aleatoria
        Question q = Question.getRandomQuestion(gameId);
        request.session().attribute("question_id",q.getQId()); // set id pregunta en la sesion
        // Insercion de datos de la pregunta en el modelo
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
        // Se buscan los 10 usuarios con mayor puntaje
        List<User> ranking = User.findBySQL("SELECT * FROM users ORDER BY rightAnswers DESC LIMIT 10");
        User u = ranking.get(0);
        // Se inserta la lista en el modelo
        model.put("top10",ranking);
        Base.close(); 
        return new ModelAndView(model, "./views/rankingView.mustache");
    }, new MustacheTemplateEngine());

    // Cierre de sesion: Se destruye la session y se retorna el menu principal de la pagina
    post("/logOut", (request, response) -> {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        Map model = new HashMap();
        // Se destruye la sesion en caso de que exista
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