
package trivia;

import org.javalite.activejdbc.Base;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.json.JSONArray;

public class UserController {


    public static ModelAndView checkSignUp(Request request, Response response) {
        Map model = new HashMap();
        String username = request.queryParams("username");
        List<User> p = User.where("username = '" + username + "'");
        if (p.size()!=0) {
            model.put("error", "El usuario " + username + " ya existe.");
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        String password = request.queryParams("password");
        String passwordRep = request.queryParams("passwordRep");
        if (!password.equals(passwordRep)) {
            model.put("error", "Las contraseñas ingresadas no coinciden.");
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        String email = request.queryParams("email");
        p = User.where("email = '" + email + "'");
        if (p.size()!=0) {
            model.put("error", "Ya existe un usuario registrado con este e-mail.");
            return new ModelAndView(model, "./views/sign_up.mustache");
        }
        User u = new User();
        u.setSignUpData(username, password, email);
        model.put("username",request.queryParams("username"));
        return new ModelAndView(model, "./views/sign_up_check.mustache");
    }
}
