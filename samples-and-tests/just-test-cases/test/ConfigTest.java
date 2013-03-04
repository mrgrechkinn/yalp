
import yalp.*;
import org.junit.Test;
import yalp.test.UnitTest;
import java.io.File;

public class ConfigTest extends UnitTest {

    @Test
    public void testIncludedConfig() {
        assertEquals("a", Yalp.configuration.get("included_a"));
        assertNull(Yalp.configuration.get("%test.included_b"));
        assertEquals("b", Yalp.configuration.get("included_b"));
        assertEquals(Yalp.frameworkPath, new File((String)Yalp.configuration.get("included_c")));
    }
}
