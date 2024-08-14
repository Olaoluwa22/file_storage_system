package file.storage_system.exception;

import file.storage_system.util.ConstantMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse<?>> defaultException(Exception exception) {
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>(ConstantMessages.INTERNAL_ERROR.getMessage(), 500, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> fileNotFoundException(FileNotFoundException exception){
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatus_code(HttpStatus.NOT_FOUND.value());
        exceptionResponse.setMessage(ConstantMessages.FILE_NOT_FOUND.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> emptyFileException(EmptyFileException exception){
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatus_code(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ConstantMessages.FILE_IS_EMPTY.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateFileExistException.class)
    public ResponseEntity<?> duplicateFileExistException(DuplicateFileExistException exception){
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatus_code(HttpStatus.FORBIDDEN.value());
        exceptionResponse.setMessage(ConstantMessages.DUPLICATE_FILE.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}