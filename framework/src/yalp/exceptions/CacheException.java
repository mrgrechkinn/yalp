package yalp.exceptions;

import java.util.Arrays;
import java.util.List;

import yalp.classloading.ApplicationClasses.ApplicationClass;
import yalp.Yalp;

/**
 * Cache related exception
 */
public class CacheException extends YalpException {

    String sourceFile;
    List<String> source;
    Integer line;

    public CacheException(String message, Throwable cause) {
        super(message, cause);
        StackTraceElement element = getInterestingStrackTraceElement(cause);
        if (element != null) {
            ApplicationClass applicationClass = Yalp.classes.getApplicationClass(element.getClassName());
            if (applicationClass.javaFile != null)
                sourceFile = applicationClass.javaFile.relativePath();
            if (applicationClass.javaSource != null)
                source = Arrays.asList(applicationClass.javaSource.split("\n"));
            line = element.getLineNumber();
        }
    }

    @Override
    public String getErrorTitle() {
        return "Cache error";
    }

    @Override
    public String getErrorDescription() {
        return getMessage();
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public List<String> getSource() {
        return source;
    }

    public Integer getLineNumber() {
        return line;
    }

    @Override
    public boolean isSourceAvailable() {
        return sourceFile != null;
    }
}
