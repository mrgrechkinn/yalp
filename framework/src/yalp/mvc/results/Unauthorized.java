package yalp.mvc.results;

import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 401 Unauthorized
 */
public class Unauthorized extends Result {
    
    String realm;
    
    public Unauthorized(String realm) {
        super(realm);
        this.realm = realm;
    }

    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.UNAUTHORIZED;
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
    }
}
