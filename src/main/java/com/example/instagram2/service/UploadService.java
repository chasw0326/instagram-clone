package com.example.instagram2.service;

import org.springframework.web.multipart.MultipartFile;

/**<strong>구현클래스에 문서화</strong><br>
 * {@link com.example.instagram2.service.serviceImpl.UploadServiceImpl}
 */
public interface UploadService {

    String uploadFile(MultipartFile uploadFile, String uploadPath);

}