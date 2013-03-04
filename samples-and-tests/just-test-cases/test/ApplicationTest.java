import org.junit.*;
import yalp.test.*;
import yalp.mvc.*;
import yalp.mvc.Http.*;
import models.*;
import yalp.Yalp;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(yalp.Yalp.defaultWebEncoding, response);
    }

    @Test
    public void testSimpleStatusCode() {
        Response response = GET("/application/simplestatuscode");
        assertStatus(204, response);
    }
    
    @Test
    public void testGettingUTF8FromConfig() {
        assertEquals("欢迎", Yalp.configuration.getProperty("utf8value"));
    }
    
    @Test
    public void testFastTag_render() {
        Response response = GET("/application/fastTag_render_test");
        assertContentEquals("OuterInnerRenderPart", response);
    }

}

