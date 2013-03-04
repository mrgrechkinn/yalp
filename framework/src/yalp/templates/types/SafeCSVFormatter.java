package yalp.templates.types;

import org.apache.commons.lang.StringEscapeUtils;
import yalp.templates.SafeFormatter;
import yalp.templates.Template;

public class SafeCSVFormatter implements SafeFormatter {

    public String format(Template template, Object value) {
        if (value != null) {
            return StringEscapeUtils.escapeCsv(value.toString());   
        }
        return "";
    }
}
