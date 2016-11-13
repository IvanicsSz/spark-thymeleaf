package spark.template.thymeleaf.example;

import model.Person;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Request;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public final class ThymeleafExample {

    private static Map<String, Object> model = new HashMap<>();

    public static void main(final String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        port(5000);
        get("/", (request, response) -> render(request));
        get("/index", ((request, response) -> renderTemplate("index", null)));
        post("/", (request, response) -> postRender(request));

        enableDebugScreen();

    }

    private static String postRender(Request request){
       //request.session().attribute();
        String fn = request.queryParams("firstname");
        request.session(true);
        request.session().attribute("user", fn);
        Map<String, Object> model2 = new HashMap<>();
        model2.put("firstname", fn);
        model2.put("user", request.session().attribute("user").toString());

        return renderTemplate("hello", model);
    }
    private static String render(Request request){
        Person bela = new Person("Bela", 23);
        String statusStr = request.queryParams("status");
        request.session().attribute(bela.name);
        System.out.println("attributes"+request.attributes());

        model.put("message", "Hello Thymeleaf!");
        model.put("bela", bela);
        model.put("user", request.session().attribute("user"));
        System.out.println("statusStr = " + statusStr);
        return renderTemplate("hello", model);
    }

    private static String renderTemplate(String template, Map model) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, template));
    }

}