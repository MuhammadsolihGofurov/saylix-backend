package api.saylix.uz.exps;

import api.saylix.uz.dto.exps.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppBadException.class)
    public ResponseEntity<ErrorResponse> handleAppBadException(
            AppBadException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_BAD_REQUEST,
                ex.getMessage(),
                "BAD_REQUEST",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpServletResponse.SC_BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(
            Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Server xatosi",
                "INTERNAL_ERROR",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
