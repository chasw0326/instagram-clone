package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.exception.myException.IllegalFileException;
import com.example.instagram2.service.UploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Service
@Log4j2
public class UploadServiceImpl implements UploadService {

    @Override
    public String uploadFile(MultipartFile uploadFile, String uploadPath) {

        if (uploadFile == null) {
            throw new IllegalFileException("uploadFile이 없습니다.");
        }
        if (!uploadFile.getContentType().startsWith("image")) {
            log.warn("이 파일은 이미지 타입이 아닙니다.");
            throw new IllegalFileException("이 파일은 이미지 타입이 아닙니다.");
        }

        // 브라우저에서 전체 경로가 들어오므로, 실제파일 이름
        String originalName = uploadFile.getOriginalFilename();
        String fileName = originalName.substring(originalName.
                lastIndexOf("\\") + 1);

        log.info("fileName: " + fileName);
        String folderPath = makeFolder(uploadPath);

        String uuid = UUID.randomUUID().toString();

        String saveName = uploadPath + File.separator + folderPath +
                File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(saveName);

        try {
            uploadFile.transferTo(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveName;
    }

    public String makeFolder(String uploadPath) {
        String str = LocalDate.now().format(
                DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);


        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}

