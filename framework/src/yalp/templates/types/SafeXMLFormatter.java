package yalp.templates.types;

import org.apache.commons.lang.StringEscapeUtils;
import yalp.templates.SafeFormatter;
import yalp.templates.TagContext;
import yalp.templates.Template;

public class SafeXMLFormatter implements SafeFormatter {

    public String format(Template template, Object value) {
        if (value != null) {
            if (TagContext.hasParentTag("verbatim")) {
                return value.toString();
            }
            return StringEscapeUtils.escapeXml(value.toString());
        }
        return "";
    }
}
