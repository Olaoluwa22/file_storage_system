package file.storage_system.util;

import lombok.Getter;

@Getter
public enum ConstantMessages {

    UNSUCCESSFUL("Unsuccessful", 1),
    FILE_NOT_FOUND("File not found", 2),
    INTERNAL_ERROR("Something went wrong",3),
    FILE_SUCCESSFULLY_UPLOADED("File uploaded successfully", 4),
    FILE_IS_EMPTY("File should not be empty",5);

    public String message;
    public int status;

    ConstantMessages(String message, int status){
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConstantMessages{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
