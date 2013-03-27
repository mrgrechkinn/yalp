package yalp.test;

import org.junit.Rule;
import org.junit.runner.RunWith;
import yalp.db.jpa.JPA;
import yalp.exceptions.UnexpectedException;

@RunWith(YalpJUnitRunner.class)
public class BaseTest extends org.junit.Assert {

    @Rule
    public YalpJUnitRunner.StartYalp startYalpBeforeTests = YalpJUnitRunner.StartYalp.rule();

    /**
     * Pause the current thread
     */
    public void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new UnexpectedException(ex);
        }
    }

    /**
     * Flush and clear the JPA session
     */
    @Deprecated
    public void clearJPASession() {
        JPA.em().flush();
        JPA.em().clear();
    }

}
