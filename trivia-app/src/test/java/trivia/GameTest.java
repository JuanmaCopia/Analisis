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
}
