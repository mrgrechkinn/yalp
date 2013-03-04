package yalp.mvc.results;

import org.w3c.dom.Document;

import yalp.exceptions.UnexpectedException;
import yalp.libs.XML;
import yalp.mvc.Http.Request;
import yalp.mvc.Http.Response;

import com.thoughtworks.xstream.XStream;

/**
 * 200 OK with a text/xml
 */
public class RenderXml extends Result {

    String xml;

    public RenderXml(CharSequence xml) {
        this.xml = xml.toString();
    }

    public RenderXml(Document document) {
        this.xml = XML.serialize(document);
    }

    public RenderXml(Object o, XStream xstream) {
        this.xml = xstream.toXML(o);
    }

    public RenderXml(Object o) {
        this(o, new XStream());
    }

    public void apply(Request request, Response response) {
        try {
            setContentTypeIfNotSet(response, "text/xml");
            response.out.write(xml.getBytes(getEncoding()));
        } catch(Exception e) {
            throw new UnexpectedException(e);
        }
    }

}
