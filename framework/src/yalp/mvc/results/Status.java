package yalp.mvc.results;

import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

public class Status extends Result {

    int code;

    public Status(int code) {
        super(code+"");
        this.code = code;
    }

    public void apply(Request request, Response response) {
        response.status = code;
    }
}