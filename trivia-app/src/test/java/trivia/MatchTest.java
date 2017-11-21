package trivia;
import trivia.Match;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchTest {
    @Before
    public void before() {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("MatchTest setup");
        Base.openTransaction();
    }

    @After
    public void after() {
        System.out.println("MatchTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    //Validates presence of the user1's Id.
    @Test
    public void validatePresenceOfUser1Id() {
        Match m = new Match();
        m.set("user1Id", null);
        m.set("user2Id", 2);
        m.set("user1Score", 0);
        m.set("user2Score", 0);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates presence of the user2's Id.
    @Test
    public void validatePresenceOfUser2Id() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", null);
        m.set("user1Score", 0);
        m.set("user2Score", 0);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates presence of the user1's score.
    @Test
    public void validatePresenceOfUser1Score() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", 2);
        m.set("user1Score", null);
        m.set("user2Score", 0);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates presence of the user2's score.
    @Test
    public void validatePresenceOfUser2Score() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", 2);
        m.set("user1Score", 0);
        m.set("user2Score", null);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates presence of the match state.
    @Test
    public void validatePresenceOfState() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", 2);
        m.set("user1Score", 0);
        m.set("user2Score", 0);
        m.set("state", "");
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates the range of user1Score.
    @Test
    public void validateRangeOfUser1Score() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", 2);
        m.set("user1Score", -1);
        m.set("user2Score", 0);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
        m.set("user1Score", 16);
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates the range of user2Score.
    @Test
    public void validateRangeOfUser2Score() {
        Match m = new Match();
        m.set("user1Id", 1);
        m.set("user2Id", 2);
        m.set("user1Score", 0);
        m.set("user2Score", -1);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
        m.set("user2Score", 16);
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates the state of the match according to the presence of winnerId.
    @Test
    public void validateGameOverState() {
        Match m = new Match();
        m.set("winnerId", 1);
        m.set("state", "Game_In_Progress");
        m.save();
        assertEquals(m.isValid(), false);
        m.set("winnerId", null);
        m.set("state", "Game_Over");
        m.save();
        assertEquals(m.isValid(), false);
    }
}