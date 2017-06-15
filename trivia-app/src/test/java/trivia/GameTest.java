package trivia;

import trivia.Game;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("GameTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("GameTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void validatePresenceOfUserId(){
        Game g = new Game();
        g.set("user_id", "");
        g.save();
        assertEquals(g.isValid(), false);
    }

    @Test
    public void validatePresenceOfState(){
        Game g = new Game();
        g.set("state", "");
        g.save();
        assertEquals(g.isValid(), false);
    }
/*
    @Test
	public void playGameAnswerOk(){
		User user = new User();
		String username = "userN";
		String password = "userN";
		String email = "userN@gmail.com";
		user.setSignUpData(username, password, email);
		Game game = new Game();
		game.setBeginning(user.getUId());
		Category c =new Category();
		c.setCategoryName("nuevaCat");
		Question q = new Question();
		q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        Random r = new Random();
		while(game.getInteger("cantPreg")<10) {
			// Busco pregunta aleatoria
			q = Question.findById(r.nextInt((Question.count()).intValue() - 1) + 1);
			// Le pregunto al usuario
			int answer = user.answerOk(q);
			game.answerQuestion(q.getQId(), user.getUId(), game.getGId(), answer);
			game.increaseNumberQuestion();
		}
		game.gameOver();
		assertEquals(game.isValid(), game.getRightAnswers()==10);
		assertEquals(game.isValid(), game.getWrongAnswers()==0);
		assertEquals(game.isValid(), game.getState()=="Game_Over");
	}
*/
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
			System.out.println("Categoria: "+ Question.getCategoryName(q));
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
