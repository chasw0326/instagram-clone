package com.example.instagram2.serviceTests;

import com.example.instagram2.exception.myException.InvalidFileException;
import com.example.instagram2.service.UploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UploadSvcTests {

    @Autowired
    private UploadService uploadService;

    @Value("${instagram.upload.path}")
    private String uploadPath;

    @DisplayName("업로드 파일이 null 일때")
    @Test
    void Should_ThrowException_WhenUploadFileIsNull() {

        Throwable ex = assertThrows(InvalidFileException.class, () -> {
            uploadService.uploadFile(null, uploadPath);
        });
        assertEquals("uploadFile이 없습니다.", ex.getMessage());
    }

    @DisplayName("이미지 파일이 아닐때")
    @Test
    void Should_ThrowException_WhenUploadFileIsNotImageType() throws IOException{
        MockMultipartFile file = new MockMultipartFile("testImage",
                "testImg.png",
                //pdf 파일일때
                "application/pdf",
                new FileInputStream("C:\\upload\\image_storage\\abc.png"));

        Throwable ex = assertThrows(InvalidFileException.class, () -> {
            uploadService.uploadFile(file, uploadPath);
        });
        assertEquals("이 파일은 이미지 타입이 아닙니다. 현재 타입: application/pdf" , ex.getMessage());
    }
}
