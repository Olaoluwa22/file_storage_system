package file.storage_system.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    ResponseEntity<?> uploadFile(MultipartFile file) throws IOException;
    ResponseEntity<byte[]> downloadFile(String fileName);
}