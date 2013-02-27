package yalp.data.binding.types;

import yalp.Logger;
import yalp.data.Upload;
import yalp.data.binding.Binder;
import yalp.data.binding.TypeBinder;
import yalp.db.Model;
import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http.Request;
import yalp.mvc.Scope.Params;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class UploadBinder implements TypeBinder<Model.BinaryField> {

    @SuppressWarnings("unchecked")
    public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        try {
            List<Upload> uploads = (List<Upload>) Request.current().args.get("__UPLOADS");
            for (Upload upload : uploads) {
                if (upload.getFieldName().equals(value) && upload.getSize() > 0) {
                    return upload;
                }
            }
            if (Params.current().get(value + "_delete_") != null) {
                return null;
            }
            return Binder.MISSING;
        } catch (Exception e) {
            Logger.error("", e);
            throw new UnexpectedException(e);
        }
    }
}
