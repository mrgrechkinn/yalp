import org.junit.Test;
import yalp.libs.WS;
import yalp.mvc.Http;
import yalp.mvc.Router;
import yalp.test.FunctionalTest;
import yalp.test.UnitTest;

public class DataBindingTest extends FunctionalTest {

    @Test
    public void testThatBindingWithQueryStringAndBodyWorks() {
        Http.Response response = POST("/DataBinding/myInputStream?productCode=XXX", "text/plain", "A_body");

        assertIsOk(response);
        assertContentEquals("XXX - A_body", response);
    }


    @Test
    public void testBindingList() {
        Http.Response response = POST("/DataBinding/myList?items[0].id=23&items[10].id=1&items[1].id=12&items[2].id=43&items[6].id=35&items[8].id=32", "text/plain", "A_body");

        assertIsOk(response);
        assertContentEquals("MyBook[23],MyBook[12],MyBook[43],null,null,null,MyBook[35],null,MyBook[32],null,MyBook[1]", response);
    }



}

