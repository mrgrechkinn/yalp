package yalp.data.binding.types;

import yalp.data.Upload;
import yalp.data.binding.TypeBinder;
import yalp.mvc.Http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Bind byte[] form multipart/form-data request.
 */
public class ByteArrayBinder implements TypeBinder<byte[]> {

    @SuppressWarnings("unchecked")
    public byte[] bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        List<Upload> uploads = (List<Upload>) Request.current().args.get("__UPLOADS");
        for (Upload upload : uploads) {
            if (upload.getFieldName().equals(value)) {
                return upload.asBytes();
            }
        }
        return null;
    }
}
