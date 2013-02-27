package yalp.mvc.results;

import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 400 Bad Request
 */
public class BadRequest extends Result {

    @Override
    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.BAD_REQUEST;
    }

}
