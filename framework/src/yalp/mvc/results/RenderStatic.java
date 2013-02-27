package yalp.mvc.results;

import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;
import yalp.vfs.VirtualFile;

public class RenderStatic extends Result {

    public String file;
    public VirtualFile resolvedFile;

    public RenderStatic(String file) {
        this.file = file;
    }

    @Override
    public void apply(Request request, Response response) {
    }

}
