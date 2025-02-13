package arep.server.app;

import arep.server.app.notations.GetMapping;
import arep.server.app.notations.RequestParam;
import arep.server.app.notations.RestController;

@RestController
public class GreetController {
    @GetMapping("/greet")
    public String greet(@RequestParam(value = "name", DefaultValue = "Guest") String name) {
        System.out.println("Hello " + name);
        return "Hello " + name;
    }
}
