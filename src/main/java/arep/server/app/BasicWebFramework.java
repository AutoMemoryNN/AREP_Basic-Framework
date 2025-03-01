package arep.server.app;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ClassicHttpResponse;

import arep.server.app.notations.GetMapping;
import arep.server.app.notations.PostMapping;
import arep.server.app.notations.RequestParam;
import arep.server.app.notations.RestController;
import arep.server.app.types.HttpMethod;

public class BasicWebFramework {
    public HashMap<String, HttpMethod> routeHandler;
    private String[] args;

    private static BasicWebFramework instance;

    public static BasicWebFramework initailize(String[] args) {
        if (instance == null) {
            instance = new BasicWebFramework(args);
        }
        return instance;
    }

    public static BasicWebFramework getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BasicWebFramework not initialized. Call initialize() first.");
        }
        return instance;
    }

    private BasicWebFramework(String[] args) {
        this.args = args;
        routeHandler = new HashMap<>();
        bindFromNotation();
    }

    private void bindFromNotation() {
        try {
            String controllerPackage = args[0];
            System.out.println(controllerPackage);
            Class<?> c = Class.forName(controllerPackage);

            if (!c.isAnnotationPresent(RestController.class)) {
                System.out.println("The class is not a RestController");
                System.exit(0);
            }

            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    routeHandler.put(getMapping.value(), new HttpMethod("GET", method));
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    routeHandler.put(postMapping.value(), new HttpMethod("POST", method));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Procesa una solicitud GET y asigna valores a los parámetros anotados
     * con @RequestParam.
     */
    public void invokeControllerMethod(String reqMethod, String path, Map<String, String> queryParams,
            ClassicHttpResponse response) {
        try {
            Method method = routeHandler.get(path).getFunction();
            System.out.println(path);
            if (method == null || !routeHandler.get(path).getMethod().equals(reqMethod)) {
                response.setCode(404);
                return;
            }

            Object controllerInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
            Parameter[] parameters = method.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                if (param.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = param.getAnnotation(RequestParam.class);
                    String paramName = requestParam.value();
                    String defaultValue = requestParam.DefaultValue();
                    args[i] = queryParams.getOrDefault(paramName, defaultValue);
                }
            }
            method.invoke(controllerInstance, args);

        } catch (Exception e) {
            e.printStackTrace();
            response.setCode(500);
        }
    }

}
