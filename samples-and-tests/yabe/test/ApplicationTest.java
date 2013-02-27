import org.junit.*;
import yalp.test.*;
import yalp.mvc.*;
import yalp.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(yalp.Yalp.defaultWebEncoding, response);
    }
    
    @Test
    public void testAdminSecurity() {
        Response response = GET("/admin");
        assertStatus(302, response);
        assertHeaderEquals("Location", "/login", response);
    }
    
}
