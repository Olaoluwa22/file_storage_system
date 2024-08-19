package file.storage_system.unitTest;

import file.storage_system.entity.File;
import file.storage_system.exception.DuplicateFileExistException;
import file.storage_system.exception.EmptyFileException;
import file.storage_system.exception.FileNotFoundException;
import file.storage_system.repository.FileRepository;
import file.storage_system.serviceImpl.FileServiceImpl;
import file.storage_system.util.ConstantMessages;
import file.storage_system.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FileServiceUnitTest {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void uploadFile_Success() throws IOException {
        String fileName = "testFile.jpeg";
        String successMessage = ConstantMessages.FILE_SUCCESSFULLY_UPLOADED.getMessage();
        String expectedResponse = successMessage + ": " + fileName;

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn("File content".getBytes());
        when(fileRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        ResponseEntity<?> response = fileService.uploadFile(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(fileRepository, times(1)).save(any(File.class));
    }
    @Test
    public void uploadFile_EmptyFile_ThrowsEmptyFileException() {
        when(multipartFile.isEmpty()).thenReturn(true);

        EmptyFileException exception = assertThrows(EmptyFileException.class, () -> {
            fileService.uploadFile(multipartFile);
        });

        assertEquals("File is empty!", exception.getMessage());
        verify(fileRepository, never()).save(any(File.class));
    }
    @Test
    public void uploadFile_DuplicateFile_ThrowsDuplicateFileExistException() {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("testFile.jpeg");
        when(fileRepository.findByFileName("testFile.jpeg")).thenReturn(Optional.of(new File()));

        DuplicateFileExistException exception = assertThrows(DuplicateFileExistException.class, () -> {
            fileService.uploadFile(multipartFile);
        });

        assertEquals("Duplicate file!", exception.getMessage());
        verify(fileRepository, never()).save(any(File.class));
    }
    @Test
    void downloadFile_FileExists_ReturnsFileData() {
        String fileName = "test.png";
        byte[] fileData = "compressedData".getBytes();
        byte[] decompressedData = "decompressedData".getBytes();

        File mockFile = File.builder()
                .fileName(fileName)
                .fileData(fileData)
                .fileType("image/png")
                .build();

        when(fileRepository.findByFileName(fileName)).thenReturn(Optional.of(mockFile));
        try (MockedStatic<FileUtil> mockedFileUtil = mockStatic(FileUtil.class)) {
            mockedFileUtil.when(() -> FileUtil.decompressFile(fileData)).thenReturn(decompressedData);

            ResponseEntity<byte[]> response = fileService.downloadFile(fileName);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
            assertEquals(decompressedData, response.getBody());

            verify(fileRepository, times(1)).findByFileName(fileName);
        }
    }
    @Test
    void downloadFile_FileDoesNotExist_ThrowsFileNotFoundException() {
        String fileName = "nonexistent.png";

        when(fileRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            fileService.downloadFile(fileName);
        });

        assertEquals("File not found", exception.getMessage());
        verify(fileRepository, times(1)).findByFileName(fileName);
    }
}