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
        m.save();
        assertEquals(m.isValid(), false);
    }

    //Validates presence of the user2's Id.
    @Test
    public void validatePresenceOfUser2Id() {
        Match m = new Match();
        m.set("user2Id", null);
        m.save();
        assertEquals(m.isValid(), false);
    }
/*
    //Validates the Table is full (the guest joined in).
    @Test
    public void validateTableIsFull() {
        Table table = new Table();
        table.set("owner_id", null);
        table.set("guest_id", null);
        table.set("is_full", true);
        table.save();
        assertEquals(table.isValid(), false);
        table.set("owner_id", 1);
        table.set("guest_id", 2);
        table.set("is_full", true);
        table.save();
        assertEquals(table.isValid(), true);
    }

    //A owner user should leave his current table before creating another one.
    @Test
    public void validateUniquenessOfOwnerId() {
        Table t = new Table();
        t.set("owner_id",1);
        t.saveIt();
        Table t2 = new Table();
        t2.set("owner_id",1);
        t2.save();
        assertEquals(t2.isValid(),false);
        assertEquals(t2.errors().get("owner_id"), "You are inside a Table as a owner already.");
    }
    
    //A guest user should leave his current table before creating another one as a owner.
    @Test
    public void validateUniquenessOfUserId() {
        Table t = new Table();
        t.set("owner_id",1);
        t.set("guest_id", 2);
        t.set("is_full", true);
        t.saveIt();
        Table t2 = new Table();
        t2.set("owner_id",2);
        t2.save();
        assertEquals((t.getInteger("guest_id")!=t2.getInteger("owner_id")),false);
    }

    //A owner user should leave his current table before joining another one as a guest.
    @Test
    public void validateUniquenessOfUserId2() {
        Table t = new Table();
        t.set("owner_id",1);
        t.saveIt();
        Table t2 = new Table();
        t2.set("owner_id",2);
        t2.set("guest_id", 1);
        t2.set("is_full", true);
        t2.save();
        assertEquals((t.getInteger("owner_id")!=t2.getInteger("guest_id")),false);
    }*/
}