package yalp.mvc.results;

import java.util.Map;

import yalp.Yalp;
import yalp.exceptions.UnexpectedException;
import yalp.libs.MimeTypes;
import yalp.mvc.Http;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;
import yalp.mvc.Scope;
import yalp.templates.TemplateLoader;

/**
 * 404 not found
 */
public class NotFound extends Result {

    /**
     * @param why a description of the problem
     */
    public NotFound(String why) {
        super(why);
    }

    /**
     * @param method routed method
     * @param path   routed path
     */
    public NotFound(String method, String path) {
        super(method + " " + path);
    }

    public void apply(Request request, Response response) {
        response.status = Http.StatusCode.NOT_FOUND;
        String format = request.format;
        if (request.isAjax() && "html".equals(format)) {
            format = "txt";
        }
        response.contentType = MimeTypes.getContentType("xx." + format);
        Map<String, Object> binding = Scope.RenderArgs.current().data;
        binding.put("result", this);
        binding.put("session", Scope.Session.current());
        binding.put("request", Http.Request.current());
        binding.put("flash", Scope.Flash.current());
        binding.put("params", Scope.Params.current());
        binding.put("yalp", new Yalp());
        String errorHtml = "Not found";
        try {
            errorHtml = TemplateLoader.load("errors/404." + (format == null ? "html" : format)).render(binding);
        } catch (Exception e) {
        }
        try {
            response.out.write(errorHtml.getBytes(getEncoding()));
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }
}
