package yalp.data.binding.types;

import yalp.data.Upload;
import yalp.data.binding.TypeBinder;
import yalp.db.Model;
import yalp.mvc.Http.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Bind file form multipart/form-data request to an array of Upload object. This is useful when you have a multiple on
 * your input file.
 */
public class UploadArrayBinder implements TypeBinder<Model.BinaryField[]> {

    @SuppressWarnings("unchecked")
    public Upload[] bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) {
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        List<Upload> uploads = (List<Upload>) Request.current().args.get("__UPLOADS");
        List<Upload> uploadArray = new ArrayList<Upload>();

        for (Upload upload : uploads) {
            if (upload.getFieldName().equals(value)) {
                uploadArray.add(upload);
            }
        }
        return uploadArray.toArray(new Upload[uploadArray.size()]);
    }
}
