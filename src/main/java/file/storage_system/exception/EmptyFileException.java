package file.storage_system.exception;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(String message){
        super(message);
    }
}
