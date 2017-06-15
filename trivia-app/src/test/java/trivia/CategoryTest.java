package trivia;
import trivia.Category;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("CategoryTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("CategoryTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void validatePresenceOfCategoriesNames(){
        Category cat = new Category();
        cat.set("name", "");
        assertEquals(cat.isValid(), false);
    }

    @Test
    public void validateUniquenessOfCategoriesNames(){
        Category cat = new Category();
        cat.set("name", "Deportes Acuaticos");
        cat.saveIt();
        Category cat2 = new Category();
        cat2.set("name", "Deportes Acuaticos");
        cat2.save();
        assertEquals(cat2.isValid(), false);
        assertEquals(cat2.errors().get("name"), "This name is already taken.");
    }
}