package yalp.exceptions;

public class ContinuationsException extends YalpException {

    public ContinuationsException(String message) {
        super(message);
    }

    @Override
    public String getErrorTitle() {
        return "await/Continuations error";
    }

    @Override
    public String getErrorDescription() {
        return String.format("A await/Continuations error occured : <strong>%s</strong>", getMessage());
    }

}
