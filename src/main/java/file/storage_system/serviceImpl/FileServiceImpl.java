package file.storage_system.serviceImpl;

import file.storage_system.entity.File;
import file.storage_system.exception.DuplicateFileExistException;
import file.storage_system.exception.EmptyFileException;
import file.storage_system.exception.FileNotFoundException;
import file.storage_system.repository.FileRepository;
import file.storage_system.service.FileService;
import file.storage_system.util.ConstantMessages;
import file.storage_system.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()){
            throw new EmptyFileException("File is empty!");
        }
        Optional<File> databaseFile = fileRepository.findByFileName(file.getOriginalFilename());
        if (databaseFile.isEmpty()) {
            fileRepository.save(File.builder().fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileData(FileUtil.compressFile(file.getBytes())).build());
        }
        else{
            throw new DuplicateFileExistException("Duplicate file!");
        }

        String successMessage = ConstantMessages.FILE_SUCCESSFULLY_UPLOADED.getMessage();
        return new ResponseEntity<>(successMessage+ ": " + file.getOriginalFilename(), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<byte[]> downloadFile(String fileName) {
        Optional<File> optionalFile = Optional.ofNullable(fileRepository.findByFileName(fileName).orElseThrow(() -> new FileNotFoundException("File not found")));
        byte[] file = FileUtil.decompressFile(optionalFile.get().getFileData());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(file);
    }
}