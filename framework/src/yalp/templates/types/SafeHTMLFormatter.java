package yalp.templates.types;

import yalp.templates.SafeFormatter;
import yalp.templates.TagContext;
import yalp.templates.Template;
import yalp.utils.HTML;

public class SafeHTMLFormatter implements SafeFormatter {

    public String format(Template template, Object value) {
        if (value != null) {
            if (TagContext.hasParentTag("verbatim")) {
                return value.toString();
            }
            return HTML.htmlEscape(value.toString());
        }
        return "";
    }
}
