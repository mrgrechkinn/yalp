package yalp.data.binding.types;

import yalp.data.binding.Binder;
import yalp.data.binding.TypeBinder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import yalp.data.Upload;
import yalp.db.Model;
import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http.Request;
import yalp.mvc.Scope.Params;

public class BinaryBinder implements TypeBinder<Model.BinaryField> {

    @SuppressWarnings("unchecked")
    public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        try {
            Model.BinaryField b = (Model.BinaryField) actualClass.newInstance();
            List<Upload> uploads = (List<Upload>) Request.current().args.get("__UPLOADS");
            for (Upload upload : uploads) {
                if (upload.getFieldName().equals(value) && upload.getSize() > 0) {
                    b.set(upload.asStream(), upload.getContentType());
                    return b;
                }
            }
            if (Params.current().get(value + "_delete_") != null) {
                return null;
            }
            return Binder.MISSING;
        } catch (Exception e) {
            throw new UnexpectedException(e);
        }
    }
}
