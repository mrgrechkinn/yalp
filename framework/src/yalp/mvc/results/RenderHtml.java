package yalp.mvc.results;

import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 200 OK with a text/plain
 */
public class RenderHtml extends Result {

    String text;

    public RenderHtml(CharSequence text) {
        this.text = text.toString();
    }

    public void apply(Request request, Response response) {
        try {
            setContentTypeIfNotSet(response, "text/html");
            response.out.write(text.getBytes(getEncoding()));
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }

}
