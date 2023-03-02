package mangpo.server.common.exception.advice;

import lombok.extern.slf4j.Slf4j;
import mangpo.server.common.exception.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice()
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResult EntityNotFoundExHandler(EntityNotFoundException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult IllegalStateExHandler(IllegalStateException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResult badCredentialsExceptionExHandler(BadCredentialsException e) {
        log.error("[exceptionHandler] ex", e);

        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult exceptionExHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);

        return new ErrorResult(e.getMessage());
    }

}
