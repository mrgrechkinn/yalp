package yalp;
/**
 *
 */


import java.io.File;
import java.util.Properties;


import org.apache.log4j.Level;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test the Logger class. At the moment only a few methods.
 *
 * @author niels
 */
public class LoggerTest {

    private static final String APPLICATION_LOG_PATH_PROPERTYNAME = "application.log.path";

//    private static String applicationLogPath;

    private static Properties yalpConfig;

    private static File applicationPath;

    private static String id;

    private static org.apache.log4j.Logger log4j;

    /**
     * Safes the original configuration and log.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        yalpConfig = Yalp.configuration;
        applicationPath = Yalp.applicationPath;
        id = Yalp.id;
        log4j = Logger.log4j;
    }

    /**
     * Restore  the original configuration and log.
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Yalp.configuration = yalpConfig;
        Yalp.applicationPath = applicationPath;
        Yalp.id = id;
        Logger.log4j = log4j;
        if (Yalp.configuration != null) {
            Logger.init();
        }
    }

    @Before
    public void setUp() throws Exception {
        Yalp.configuration = new Properties();
        Yalp.applicationPath = new File(".");
        Yalp.id = "test";
    }

    @After
    public void tearDown() throws Exception {
    }


    /**
     * Test method for {@link yalp.Logger#init()}.
     */
    @Test
    public void testInitWithProperties() {
        Yalp.configuration.put(APPLICATION_LOG_PATH_PROPERTYNAME, "/yalp/testlog4j.properties");
        Logger.log4j = null;
        Logger.init();
        org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger("logtest.properties");
        assertEquals(Level.ERROR, log4jLogger.getLevel());
    }

    /**
     * Test method for {@link yalp.Logger#init()}.
     */
    @Test
    public void testInitWithXML() {
        Yalp.configuration.put(APPLICATION_LOG_PATH_PROPERTYNAME, "/yalp/testlog4j.xml");
        Logger.log4j = null;
        Logger.init();
        org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger("logtest.xml");
        assertEquals(Level.ERROR, log4jLogger.getLevel());
    }
}
