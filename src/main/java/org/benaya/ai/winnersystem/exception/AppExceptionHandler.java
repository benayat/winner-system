package org.benaya.ai.winnersystem.exception;

import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleValidationException(ConstraintViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(BetsAreBlockedException.class)
    public ProblemDetail handleBetsAreBlockedException(BetsAreBlockedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.LOCKED, e.getMessage());
    }

    @ExceptionHandler(LowBalanceException.class)
    public ProblemDetail handleLowBalanceException(LowBalanceException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.PRECONDITION_FAILED, e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, @Nonnull WebRequest request) {
        String errorsString = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorsString);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, @Nonnull HttpHeaders headers, @Nonnull HttpStatusCode status, @Nonnull WebRequest request) {
        return new ResponseEntity<>(new ApiError("unhandled error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
