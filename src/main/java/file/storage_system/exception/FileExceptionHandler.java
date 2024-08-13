package file.storage_system.exception;

import file.storage_system.util.ConstantMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class FileExceptionHandler {
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> emptyFileException(EmptyFileException exception){
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatus_code(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ConstantMessages.FILE_IS_EMPTY.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse<?>> defaultException(Exception exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>(ConstantMessages.INTERNAL_ERROR.getMessage(), 500, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}