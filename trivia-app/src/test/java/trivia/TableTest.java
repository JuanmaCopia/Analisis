package trivia;
import trivia.Table;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest {
    @Before
    public void before() {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("TableTest setup");
        Base.openTransaction();
    }

    @After
    public void after() {
        System.out.println("TableTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    //Validates presence of the Owner's Id.
    @Test
    public void validatePresenceOfOwnerId() {
        Table table = new Table();
        table.set("owner_id", null);
        table.save();
        assertEquals(table.isValid(), false);
    }

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

    //Debes salir de la mesa actual antes de unirte a otra
    //Ya estas dentro de una mesa como guest o como owner
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
}