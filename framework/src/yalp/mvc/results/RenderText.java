package yalp.mvc.results;

import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 200 OK with a text/plain
 */
public class RenderText extends Result {
    
    String text;
    
    public RenderText(CharSequence text) {
        this.text = text.toString();
    }

    public void apply(Request request, Response response) {
        try {
            setContentTypeIfNotSet(response, "text/plain; charset=" + Http.Response.current().encoding);
            response.out.write(text.getBytes(getEncoding()));
        } catch(Exception e) {
            throw new UnexpectedException(e);
        }
    }

}
