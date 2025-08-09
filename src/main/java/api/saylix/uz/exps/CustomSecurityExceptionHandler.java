package api.saylix.uz.exps;

import api.saylix.uz.dto.exps.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Component
public class CustomSecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper mapper;

    public CustomSecurityExceptionHandler(ObjectMapper mapper) {
        // Spring context’dan kelgan, JacksonConfig’da sozlangan mapper
        this.mapper = mapper;
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response,
                           int status, String message, String errorCode) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        response.resetBuffer();
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                message,
                errorCode,
                request.getRequestURI()
        );

        mapper.writeValue(response.getOutputStream(), errorResponse);
        response.flushBuffer();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        sendError(request, response,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Foydalanuvchi tizimga kirmagan",
                "UNAUTHORIZED");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        sendError(request, response,
                HttpServletResponse.SC_FORBIDDEN,
                "Sizga ushbu resursga ruxsat berilmagan",
                "FORBIDDEN");
    }
}
