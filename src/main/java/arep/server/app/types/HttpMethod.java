package arep.server.app.types;

import java.lang.reflect.Method;

public class HttpMethod {
    private String method;
    private Method function;

    public HttpMethod(String method, Method function) {
        this.method = method;
        this.function = function;
    }

    public Method getFunction() {
        return this.function;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setFunction(Method function) {
        this.function = function;
    }
}
