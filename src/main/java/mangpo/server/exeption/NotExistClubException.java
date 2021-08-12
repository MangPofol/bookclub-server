package mangpo.server.exeption;

public class NotExistClubException extends RuntimeException{
    public NotExistClubException() {
        super();
    }

    public NotExistClubException(String message) {
        super(message);
    }

    public NotExistClubException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistClubException(Throwable cause) {
        super(cause);
    }

    protected NotExistClubException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
