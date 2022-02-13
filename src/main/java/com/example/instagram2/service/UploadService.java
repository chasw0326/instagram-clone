package com.example.instagram2.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    String uploadFile(MultipartFile uploadFile, String uploadPath);

}