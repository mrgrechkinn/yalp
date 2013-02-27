package yalp.exceptions;

/**
 * Error in application.conf
 */
public class ConfigurationException extends YalpException {

    public ConfigurationException(String message) {
        super(message);
    }

    @Override
    public String getErrorDescription() {
        return getMessage();
    }

    @Override
    public String getErrorTitle() {
        return "Configuration error.";
    }
    
}
