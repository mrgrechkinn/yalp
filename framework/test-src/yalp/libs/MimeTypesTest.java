package yalp.libs;

import org.junit.Before;
import org.junit.Test;
import yalp.Yalp;
import yalp.mvc.Http.Response;

import java.util.Properties;

import static org.junit.Assert.assertEquals;


/**
 * Tests for {@link MimeTypes} class.
 */
public class MimeTypesTest {

    @Before
    public void setup() {
        Response resp = new Response();
        Response.current.set(resp);
        Yalp.configuration = new Properties();
    }

    @Test
    public void contentTypeShouldReturnResponseCharsetWhenAvailable() throws Exception {
        String oldEncoding = Response.current().encoding;
        try {
            Response.current().encoding = "my-response-encoding";
            assertEquals("text/xml; charset=my-response-encoding",
                    MimeTypes.getContentType("test.xml"));
        } finally {
            Response.current().encoding = oldEncoding;
        }
    }

    @Test
    public void contentTypeShouldReturnDefaultCharsetInAbsenceOfResponse() throws Exception {
        Response originalResponse = Response.current();
        try {
            Response.current.set(null);
            assertEquals("text/xml; charset=" + yalp.Yalp.defaultWebEncoding,
                    MimeTypes.getContentType("test.xml"));
        } finally {
            Response.current.set(originalResponse);
        }
    }
}
