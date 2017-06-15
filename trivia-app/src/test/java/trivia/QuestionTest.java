package trivia;
import trivia.Question;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuestionTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("QuestionTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("QuestionTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    //Valida la presencia del campo pregunta.
    @Test
    public void validatePresenceOfPregunta(){
        Question q = new Question();
        q.set("pregunta", "");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de la opcion 1.
    @Test
    public void validatePresenceOfOption1(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de la opcion 2.
    @Test
    public void validatePresenceOfOption2(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de la opcion 3.
    @Test
    public void validatePresenceOfOption3(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de la opcion 4.
    @Test
    public void validatePresenceOfOption4(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de una opcion correcta.
    @Test
    public void validatePresenceOfCorrectOption(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",null);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida la presencia de un id de categoria.
    @Test
    public void validatePresenceOfCategoryId(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",null);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida que el rango de respuesta correcta sea entre 1 y 4.
    @Test
    public void validateRangeOfCorrectOption(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",5);
        q.set("category_id",1);
        q.save();
        assertEquals(q.isValid(), false);
    }

    //Valida que la pregunta sea unica.
    @Test
    public void validateUniquenessOfPregunta(){
        Question q = new Question();
        q.set("pregunta", "ques");
        q.set("option1", "asd");
        q.set("option2", "afg");
        q.set("option3", "nba");
        q.set("option4", "kla");
        q.set("correctOption",3);
        q.set("category_id",1);
        q.saveIt();
        Question q2 = new Question();
        q2.set("pregunta", "ques");
        q2.set("option1", "asd");
        q2.set("option2", "afg");
        q2.set("option3", "nba");
        q2.set("option4", "kla");
        q2.set("correctOption",3);
        q2.set("category_id",1);
        q2.save();
        assertEquals(q2.isValid(), false);
        assertEquals(q2.errors().get("pregunta"), "This question exists already.");
    }


}