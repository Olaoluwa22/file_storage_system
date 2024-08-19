package file.storage_system.e2e;

import file.storage_system.entity.File;
import file.storage_system.repository.FileRepository;
import file.storage_system.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileServiceEnd2EndTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void testUploadFile_Success() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "testImage2.png",
                MediaType.IMAGE_PNG_VALUE,
                "This is a test file".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("File uploaded successfully")));

        // Verify that the file is stored in the database
        Optional<File> storedFile = fileRepository.findByFileName("testImage2.png");
        assertTrue(((java.util.Optional<?>) storedFile).isPresent());
        assertEquals("image/png", storedFile.get().getFileType());
    }
    @Test
    public void testUploadFile_EmptyFile_ThrowsException() throws Exception {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[0]
        );

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("File should not be empty.")));
    }
    @Test
    public void testUploadFile_DuplicateFile_ThrowsException() throws Exception {
        // Given
        fileRepository.save(File.builder().fileName("duplicate.png").fileType("image/png").fileData(new byte[10]).build());

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "duplicate.png",
                MediaType.IMAGE_PNG_VALUE,
                "This is a duplicate test file".getBytes()
        );

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(mockFile))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("File with this name already exist.")));
    }
    @Test
    public void testDownloadFile_Success() throws Exception {
        // Given
        byte[] compressedData = FileUtil.compressFile("This is a test file".getBytes());
        File file = File.builder().fileName("download.png").fileType("image/png").fileData(compressedData).build();
        fileRepository.save(file);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/download/download.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(FileUtil.decompressFile(compressedData)));
    }
    @Test
    public void testDownloadFile_FileNotFound_ThrowsException() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/download/nonexistent.png"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("File not found")));
    }

}
