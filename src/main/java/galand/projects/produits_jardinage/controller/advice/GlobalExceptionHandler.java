package galand.projects.produits_jardinage.controller.advice;


import galand.projects.produits_jardinage.exception.DuplicateProductException;
import galand.projects.produits_jardinage.exception.InsufficientStockException;
import galand.projects.produits_jardinage.exception.InvalidPriceException;
import galand.projects.produits_jardinage.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Gestion de productNotFoundException
     * */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException e, WebRequest request)
    {
        log.error("Produit non trouve: {}", e.getMessage());
        Map<String, Object> body = createErrorBody(HttpStatus.NOT_FOUND, e.getMessage(), e.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Gestion de duplicateProductException
     * */
    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<Object> handleDuplicateProductException(DuplicateProductException exception, WebRequest request){
        log.error("Produit en double: {}", exception.getMessage());
        Map<String, Object> body = createErrorBody(HttpStatus.CONFLICT, exception.getMessage(), exception.getErrorCode(), request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);

    }


    /**
     * Gestion de InsufficientStockException
     * */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException exception, WebRequest request)
    {
        log.error("Stock insuffisant: {}", exception.getMessage());
        Map<String, Object> body = createErrorBody(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Gestion de InvalidPriceException
     * */
    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<Object> handleInvalidPriceException(InvalidPriceException exception, WebRequest request)
    {
        log.error("Prix invalide: {}", exception.getMessage());
        Map<String, Object> body = createErrorBody(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                exception.getErrorCode(),
                request
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> handleGlobalException(Exception exception, WebRequest request)
    {
        log.error("Erreur inattendue: {}", exception.getMessage(),exception);
        Map<String, Object> body = createErrorBody(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne s'est produite",
                "INTERNAL_ERROR",
                request
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> createErrorBody(
            HttpStatus status,
            String message,
            String errorCode,
            WebRequest request
    ){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("errorCode", errorCode);
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return  body;
    }
}
