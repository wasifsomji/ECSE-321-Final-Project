package ca.mcgill.ecse321.hotelsystem.exception;


import jakarta.persistence.PersistenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HRSExceptionHandler {
    @ExceptionHandler(HRSException.class)
    public ResponseEntity<String> handleHRSException(HRSException e) {
        return new ResponseEntity<String>(e.getMessage(), e.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String message = "";
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            message += fe.getDefaultMessage() + "\n";
        }
        return new ResponseEntity<String>(message, e.getStatusCode());
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<String> handleIdException(MethodArgumentNotValidException e) {
        String message = "";
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            message += fe.getDefaultMessage() + "\n";
        }
        return new ResponseEntity<String>(message, e.getStatusCode());
    }

}
