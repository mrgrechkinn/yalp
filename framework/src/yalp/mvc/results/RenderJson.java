package yalp.mvc.results;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;
import yalp.exceptions.UnexpectedException;

/**
 * 200 OK with application/json
 */
public class RenderJson extends Result {

    String json;

    public RenderJson(Object o) {
        json = new Gson().toJson(o);
    }

    public RenderJson(Object o, Type type) {
        json = new Gson().toJson(o, type);
    }

    public RenderJson(Object o, JsonSerializer<?>... adapters) {
        GsonBuilder gson = new GsonBuilder();
        for (Object adapter : adapters) {
            Type t = getMethod(adapter.getClass(), "serialize").getParameterTypes()[0];
            gson.registerTypeAdapter(t, adapter);
        }
        json = gson.create().toJson(o);
    }

    public RenderJson(String jsonString) {
        json = jsonString;
    }

    public void apply(Request request, Response response) {
        try {
            String encoding = getEncoding();
            setContentTypeIfNotSet(response, "application/json; charset="+encoding);
            response.out.write(json.getBytes(encoding));
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

    //
    static Method getMethod(Class clazz, String methodName) {
        Method bestMatch = null;
        for(Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(methodName) && !m.isBridge()) {
                if (bestMatch == null || !Object.class.equals(m.getParameterTypes()[0])) {
                    bestMatch = m;
                }
            }
        }
        return bestMatch;
    }


}
