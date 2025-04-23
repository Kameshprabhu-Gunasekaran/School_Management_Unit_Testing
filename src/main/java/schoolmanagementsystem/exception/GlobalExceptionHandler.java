package schoolmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import schoolmanagementsystem.dto.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestServiceAlertException.class)
    public ResponseEntity<ResponseDTO> handleBadRequestServiceAlertException(final BadRequestServiceAlertException exception, WebRequest webRequest) {
        final ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setMessage(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(final ResourceNotFoundException exception, WebRequest webRequest) {
        final ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setMessage(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
        responseDTO.setData(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleSecurityException(final Exception exception) {
        final ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setMessage(exception.getMessage());
        responseDTO.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseDTO.setData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
}
