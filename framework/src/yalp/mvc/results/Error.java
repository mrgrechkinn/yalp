package yalp.mvc.results;

import java.util.Map;
import yalp.Logger;

import yalp.Yalp;
import yalp.exceptions.UnexpectedException;
import yalp.libs.MimeTypes;
import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;
import yalp.mvc.Scope;
import yalp.templates.TemplateLoader;

/**
 * 500 Error
 */
public class Error extends Result {

    private int status;

    public Error(String reason) {
        super(reason);
        this.status = Http.StatusCode.INTERNAL_ERROR;
    }

    public Error(int status, String reason) {
        super(reason);
        this.status = status;
    }

    public void apply(Request request, Response response) {
        response.status = status;
        String format = request.format;
        if (request.isAjax() && "html".equals(format)) {
            format = "txt";
        }
        response.contentType = MimeTypes.getContentType("xx." + format);
        Map<String, Object> binding = Scope.RenderArgs.current().data;
        binding.put("exception", this);
        binding.put("result", this);
        binding.put("session", Scope.Session.current());
        binding.put("request", Http.Request.current());
        binding.put("flash", Scope.Flash.current());
        binding.put("params", Scope.Params.current());
        binding.put("yalp", new Yalp());
        String errorHtml = getMessage();
        try {
            errorHtml = TemplateLoader.load("errors/" + this.status + "." + (format == null ? "html" : format)).render(binding);
        } catch (Exception e) {
            Logger.warn(e, "Error page caused an error");
        }
        try {
            response.out.write(errorHtml.getBytes(getEncoding()));
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }
}
