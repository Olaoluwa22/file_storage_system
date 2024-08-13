package file.storage_system.serviceImpl;

import file.storage_system.entity.File;
import file.storage_system.exception.EmptyFileException;
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
        fileRepository.save(File.builder().fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileData(FileUtil.compressFile(file.getBytes())).build());

        return new ResponseEntity<>(ConstantMessages.FILE_SUCCESSFULLY_UPLOADED.getMessage()+ ": " + file.getOriginalFilename(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String fileName) {
        Optional<File> optionalFile = fileRepository.findByFileName(fileName);
        byte[] file = FileUtil.decompressFile(optionalFile.get().getFileData());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(file);
    }
}