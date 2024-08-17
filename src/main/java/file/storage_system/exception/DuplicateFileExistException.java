package file.storage_system.exception;

public class DuplicateFileExistException extends RuntimeException{
    public DuplicateFileExistException(String message){
        super(message);
    }
}