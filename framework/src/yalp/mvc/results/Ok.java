package yalp.mvc.results;


import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 200 OK
 */
public class Ok extends Result {

    public Ok() {
        super("OK");
    }

    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.OK;
    }
}
