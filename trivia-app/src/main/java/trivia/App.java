
package trivia;

import org.javalite.activejdbc.Base;
import java.util.*;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {

    public static void main( String[] args ) {

        staticFileLocation("/public");

        /**
         * get method that returns the index view.
         * @pre. true
         * @return a Mustache view.
         * @post. The index view is returned.
         */
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "./views/index.mustache");
        }, new MustacheTemplateEngine());

        /**
         * get method that returns the sign up view.
         * @pre. true
         * @return a Mustache view.
         * @post. The sign up view is returned.
         */
        get("/sign_up", (req,res) -> {
            Map<String, Object> model = new HashMap();
            return new ModelAndView(model, "./views/sign_up.mustache");
        },new MustacheTemplateEngine());

        /**
         * get method that returns the sign in view.
         * @pre. true
         * @return a Mustache view.
         * @post. The sign in view is returned.
         */
        get("/sign_in", (req,res) -> {
            Map<String, Object> model = new HashMap();
            return new ModelAndView(model, "./views/sign_in.mustache");
        },new MustacheTemplateEngine());

        /**
         * This post method checks the sign up data, if their are correct the welcome view is returned,
         * otherwise the sign up view is returned informing the error.
         * @param username, password, passwordRep, email
         * @pre. username and email should be properly formed.
         * @return a Mustache view.
         * @post.  The welcome view is returned iff username and email are not already in the database,
         * if they are, then the sign up view must be returned showing the error.
         */
        post("/sign_up_check", (request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
            Map model = new HashMap();
            String username = request.queryParams("username");
            List<User> p = User.where("username = '" + username + "'");
            if (p.size()!=0) {
                model.put("error", "El usuario " + username + " ya existe.");
                Base.close();
                return new ModelAndView(model, "./views/sign_up.mustache");
            }
            String password = request.queryParams("password");
            String passwordRep = request.queryParams("passwordRep");
            if (!password.equals(passwordRep)) {
                model.put("error", "Las contraseñas ingresadas no coinciden.");
                Base.close();
                return new ModelAndView(model, "./views/sign_up.mustache");
            }
            String email = request.queryParams("email");
            p = User.where("email = '" + email + "'");
            if (p.size()!=0) {
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

        /**
         * This post method creates the session and returns the game's menu view if the username and password exists,
         * otherwise the sign in view is returned informing the error.
         * @param username, password.
         * @pre. username and password should be properly formed.
         * @return a Mustache view.
         * @post.  The game's menu view is returned iff username and password are already in the database,
         * if they aren't, then the sign in view must be returned showing the error.
         */
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

        /**
         * get method that returns an alternative game's menu view.
         * @pre. must be a previously created session.
         * @return a Mustache view.
         * @post. The game's menu view is returned.
         */
        get("/game2", (request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
            Map model = new HashMap();
            Base.close();
            return new ModelAndView(model, "./views/game.mustache");
        }, new MustacheTemplateEngine());

        /**
         * In this post method the game starts. It looks for a random question and returns the view that shows the question.
         * @pre. true.
         * @return a Mustache view.
         * @post.  The view that shows the question is returned.
         */
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

        /**
         * This post method checks the answer. If the answer is right, the rightAnswer view is returned,
         * otherwise the wrongAnswer view is returned. 
         * @param user_answer
         * @pre. the user must answer a question.
         * @return a Mustache view.
         * @post.  The rightAnswer view is returned iff the integer value of user_answer equals the correct option,
         * otherwise the wrongAnswer view is returned, showing which one was the right answer.
         */
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
            if (correctOption == ansNum) {
                Base.close();
                return new ModelAndView(model, "./views/rightAnswer.mustache");
            }
            else {
                switch (correctOption) {
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

        /**
         * This post method looks for the next question if the game is not over, and the view that shows the question is returned,
         * otherwise the gameFinished view is returned.
         * @pre. must be a previously answered question.
         * @return a Mustache view.
         * @post.  The view that shows the next question is returned iff the game it's not over, otherwise the gameFinished view is returned.
         */
        post("/nextQuestion", (request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
            Map model = new HashMap();
            int gameId = request.session().attribute("game_id");
            Game g = Game.findById(gameId);
            g.increaseNumberQuestion();
            if (g.getNumberQuestion()==10) {
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

        /**
         * get method that returns the ranking view.
         * @pre. true
         * @return a Mustache view.
         * @post. The ranking view is returned.
         */
        get("/ranking", (request, response) -> {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
            Map model = new HashMap();
            List<User> ranking = User.findBySQL("SELECT * FROM users ORDER BY rightAnswers DESC LIMIT 10");
            User u = ranking.get(0);
            model.put("top10",ranking);
            Base.close();
            return new ModelAndView(model, "./views/rankingView.mustache");
        }, new MustacheTemplateEngine());

        /**
         * post method that destroys the current session and returns the index view.
         * @pre. must be a previously created session.
         * @return a Mustache view.
         * @post. The index view is returned.
         */
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
