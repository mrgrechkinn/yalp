import yalp.Yalp;
import yalp.YalpPlugin;
import yalp.libs.IO;
import yalp.libs.MimeTypes;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;
import yalp.mvc.Router;
import yalp.vfs.VirtualFile;

import java.io.File;

public class DocViewerPlugin extends YalpPlugin {

    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
        if ("/@api".equals(request.path) || "/@api/".equals(request.path)) {
            response.status = 302;
            response.setHeader("Location", "/@api/index.html");
            return true;
        }
        if (request.path.startsWith("/@api/")) {
            if (request.path.matches("/@api/-[a-z]+/.*")) {
                String module = request.path.substring(request.path.indexOf("-") + 1);
                module = module.substring(0, module.indexOf("/"));
                VirtualFile f = Yalp.modules.get(module).child("documentation/api/" + request.path.substring(8 + module.length()));
                if (f.exists()) {
                    response.contentType = MimeTypes.getMimeType(f.getName());
                    response.out.write(f.content());
                }
                return true;
            }
            File f = new File(Yalp.frameworkPath, "documentation/api/" + request.path.substring(6));
            if (f.exists()) {
                response.contentType = MimeTypes.getMimeType(f.getName());
                response.out.write(IO.readContent(f));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onRoutesLoaded() {
        Router.prependRoute("GET", "/@documentation/?", "YalpDocumentation.index");
        Router.prependRoute("GET", "/@documentation/{id}", "YalpDocumentation.page");
        Router.prependRoute("GET", "/@documentation/home", "YalpDocumentation.index");
        Router.prependRoute("GET", "/@documentation/{docLang}/{id}", "YalpDocumentation.page");
        Router.prependRoute("GET", "/@documentation/images/{name}", "YalpDocumentation.image");
        Router.prependRoute("GET", "/@documentation/files/{name}", "YalpDocumentation.file");
        Router.prependRoute("GET", "/@documentation/{docLang}/images/{name}", "YalpDocumentation.image");
        Router.prependRoute("GET", "/@documentation/{docLang}/files/{name}", "YalpDocumentation.file");
        Router.prependRoute("GET", "/@documentation/{docLang}/modules/{module}/{id}", "YalpDocumentation.page");
        Router.prependRoute("GET", "/@documentation/modules/{module}/images/{name}", "YalpDocumentation.image");
        Router.prependRoute("GET", "/@documentation/modules/{module}/files/{name}", "YalpDocumentation.file");
        Router.prependRoute("GET", "/@documentation/cheatsheet/{category}", "YalpDocumentation.cheatSheet");
        Router.prependRoute("GET", "/@documentation/{docLang}/cheatsheet/{category}", "YalpDocumentation.cheatSheet");
    }

}
