package com.example.instagram2.service.serviceImpl;

import com.example.instagram2.exception.myException.InvalidFileException;
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

/**
 * <code>UploadService</code><br>
 * 업로드에 관련된 서비스<br>
 * 이미지 파일만 업로드 가능
 * yyyy/MM/dd 형식의 폴더를 생성하고 저장
 * @author chasw326
 */
@Service
@Log4j2
public class UploadServiceImpl implements UploadService {

    /**
     * 이미지파일 업로드
     * UUID로 파일이름 유일성 보장
     * @param uploadFile
     * @param uploadPath
     * InvalidArgsException과 구분을 하고 싶어서<br>
     * 따로 커스텀 exception을 구현함
     * @throws InvalidFileException
     * @return saveName
     */
    @Override
    public String uploadFile(MultipartFile uploadFile, String uploadPath) {

        if (uploadFile == null) {
            log.warn("uploadFile이 없습니다.");
            throw new InvalidFileException("uploadFile이 없습니다.");
        }
        if (!uploadFile.getContentType().startsWith("image")) {
            log.warn("이 파일은 이미지 타입이 아닙니다. 현재 타입: {}", uploadFile.getContentType());
            throw new InvalidFileException("이 파일은 이미지 타입이 아닙니다. 현재 타입: " + uploadFile.getContentType());
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

    /**
     * yyyy/MM/dd 형식의 폴더 생성
     * @param uploadPath
     * @return folderPath
     */
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

