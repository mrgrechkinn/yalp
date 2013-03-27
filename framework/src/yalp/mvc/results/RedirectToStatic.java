package yalp.mvc.results;

import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 302 Redirect
 */
public class RedirectToStatic extends Result {

    String file;

    public RedirectToStatic(String file) {
        this.file = file;
    }

    public void apply(Request request, Response response) {
        try {
            response.status = Http.StatusCode.FOUND;
            response.setHeader("Location", file);
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }
}
