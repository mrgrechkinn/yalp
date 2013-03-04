package yalp.mvc.results;

import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

/**
 * 304 Not Modified
 */
public class NotModified extends Result {

    String etag;

    public NotModified() {
        super("NotModified");
    }

    public NotModified(String etag) {
        this.etag = etag;
    }

    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.NOT_MODIFIED;
        if (etag != null) {
            response.setHeader("Etag", etag);
        }
    }
}
