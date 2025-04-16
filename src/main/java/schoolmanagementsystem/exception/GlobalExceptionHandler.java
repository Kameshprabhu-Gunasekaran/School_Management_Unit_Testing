package schoolmanagementsystem.exception;

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
        responseDTO.setStatusCode(400);
        responseDTO.setData(null);
        return ResponseEntity.ok().body(responseDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleSecurityException(final Exception exception) {
        final ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setMessage(exception.getMessage());
        responseDTO.setStatusCode(400);
        responseDTO.setData(null);
        return ResponseEntity.ok().body(responseDTO);
    }
}
