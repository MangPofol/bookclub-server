package mangpo.server.exeption;

public class NotExistBookException extends RuntimeException{
    public NotExistBookException() {
        super();
    }

    public NotExistBookException(String message) {
        super(message);
    }

    public NotExistBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistBookException(Throwable cause) {
        super(cause);
    }

    protected NotExistBookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
