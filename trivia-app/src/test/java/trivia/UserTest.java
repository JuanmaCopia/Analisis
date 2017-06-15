package trivia;
import trivia.User;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia_test", "root", "root");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    //Valida la presencia de nombre de usuario, contraseña y el e-mail.
    @Test
    public void validatePrecenseOfUsernamesAndPasswordAndEmail(){
        User user = new User();
        assertEquals(user.isValid(), false);
        user.set("username", "John");
        assertEquals(user.isValid(), false);
        user.set("password","john123");
        assertEquals(user.isValid(), false);
        user.set("email","john@gmail.com");
        assertEquals(user.isValid(), true);
    }

    //Valida que el nombre de usuario sea unico. 
    @Test
    public void validateUniquenessOfUsernames() {
        User u = new User();
        u.set("username","john");
        u.set("password","john123");
        u.set("email","john@gmail.com");
        u.saveIt();
        User u2 = new User();
        u2.set("username","john");
        u2.set("password","asd");
        u2.set("email","john2@gmail.com");
        u2.save();
        assertEquals(u2.isValid(),false);
        assertEquals(u2.errors().get("username"), "This username is already taken.");
    }

    //Valida que el e-mail de un usuario sea unico.
    @Test
    public void validateUniquenessOfEmails() {
        User u = new User();
        u.set("username","carlito");
        u.set("password","carlito");
        u.set("email","carlito@gmail.com");
        u.saveIt();
        User u2 = new User();
        u2.set("username","asd");
        u2.set("password","asd");
        u2.set("email","carlito@gmail.com");
        u2.save();
        assertEquals(u2.isValid(),false);
        assertEquals(u2.errors().get("email"), "This email is already taken.");
    } 

    //Valida que el formato del e-mail sea correcto.
    @Test
    public void validateEmailOfUser(){
        User u = new User();
        u.set("username","carlos");
        u.set("password","carlos");
        u.set("email","carlos@gmail.com");
        u.save();
        assertEquals(u.isValid(),true);
        User u2 = new User();
        u2.set("username","viejito");
        u2.set("password","viejito");
        u2.set("email","viejito-gmail.com");
        u2.save();
        assertEquals(u2.isValid(),false);
    }
}