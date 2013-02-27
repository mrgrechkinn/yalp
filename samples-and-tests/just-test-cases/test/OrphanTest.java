import org.junit.Test;
import yalp.mvc.Http;
import yalp.test.FunctionalTest;

public class OrphanTest extends FunctionalTest {

    @Test
    public void testCollection() {
        Http.Response response = POST("/CollectionOrphan/create");
        assertIsOk(response);
        String orderId = getContent(response);
        response = POST("/CollectionOrphan/update?id=" + orderId);
        assertIsOk(response);
    }

    @Test
    public void testMap() {
        Http.Response response = POST("/MapOrphan/create");
        assertIsOk(response);
        String orderId = getContent(response);
        response = POST("/MapOrphan/update?id=" + orderId);
        assertIsOk(response);
    }
}
