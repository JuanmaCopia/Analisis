package trivia;
import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model {
	public Game(int user_id) {
		int score = 0;
		User usuario = User.findById(user_id);

		for (int i = 0;i < 1 ;i++) {
			// Busco pregunta aleatoria
			Question q = Question.getRandomQuestion();
			// Me fijo que categoria tiene y la muestro
			//Integer category_id = q.getInteger("category_id");
			Category c = Category.findById(1);
			String nombreCategoria = c.getString("name");
			System.out.println("Categoria: "+nombreCategoria);
			// Le pregunto al usuario
			String preg = q.getString("pregunta");
			System.out.println(preg);
			String respuesta = usuario.responder(q);
			System.out.println("Respondio: "+respuesta);
			if (Question.esCorrecta(q,respuesta)) {
				System.out.println("La respuesta es correcta");
				usuario.set("score",usuario.getInteger("score")+10);
				usuario.saveIt();
			}
			else 
				System.out.println("La respuesta es incorrecta");
		}

	}

}