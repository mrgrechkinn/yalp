package yalp.test;

import java.io.File;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import yalp.Invoker;
import yalp.Invoker.DirectInvocation;
import yalp.Yalp;

public class YalpJUnitRunner extends Runner implements Filterable {

    public static final String invocationType = "JUnitTest";

    public static boolean useCustomRunner = false;

    // *******************
    JUnit4 jUnit4;

    public YalpJUnitRunner(Class testClass) throws ClassNotFoundException, InitializationError {
        synchronized (Yalp.class) {
            if (!Yalp.started) {
                Yalp.init(new File("."), YalpJUnitRunner.getYalpId());
                Yalp.javaPath.add(Yalp.getVirtualFile("test"));
                Yalp.start();
                useCustomRunner = true;
                Class classToRun = Yalp.classloader.loadApplicationClass(testClass.getName());
            }
            Class classToRun = Yalp.classloader.loadApplicationClass(testClass.getName());
            jUnit4 = new JUnit4(classToRun);
        }
    }

    private static String getYalpId() {
        String yalpId = System.getProperty("yalp.id", "test");
        if (!(yalpId.startsWith("test-") && yalpId.length() >= 6)) {
            yalpId = "test";
        }
        return yalpId;
    }

    @Override
    public Description getDescription() {
        return jUnit4.getDescription();
    }

    @Override
    public void run(final RunNotifier notifier) {
        jUnit4.run(notifier);
    }

    @Override
    public void filter(Filter toFilter) throws NoTestsRemainException {
        jUnit4.filter(toFilter);

    }

    // *********************
    public enum StartYalp implements MethodRule {

        INVOKE_THE_TEST_IN_YALP_CONTEXT {
            public Statement apply(final Statement base, FrameworkMethod method, Object target) {

                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                        if (!Yalp.started) {
                            Yalp.forceProd = true;
                            Yalp.init(new File("."), YalpJUnitRunner.getYalpId());
                        }

                        try {
                            Invoker.invokeInThread(new DirectInvocation() {

                                @Override
                                public void execute() throws Exception {
                                    try {
                                        base.evaluate();
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public Invoker.InvocationContext getInvocationContext() {
                                    return new Invoker.InvocationContext(invocationType);
                                }
                            });
                        } catch (Throwable e) {
                            throw ExceptionUtils.getRootCause(e);
                        }
                    }
                };
            }
        },
        JUST_RUN_THE_TEST {
            public Statement apply(final Statement base, FrameworkMethod method, Object target) {
                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                        base.evaluate();
                    }
                };
            }
        };

        public static StartYalp rule() {
            return YalpJUnitRunner.useCustomRunner ? INVOKE_THE_TEST_IN_YALP_CONTEXT : JUST_RUN_THE_TEST;
        }
    }
}
