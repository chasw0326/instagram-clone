package com.example.instagram2.service;

/**<strong>구현클래스에 문서화</strong><br>
 * {@link com.example.instagram2.service.serviceImpl.LikeServiceImpl}
 */
public interface LikesService {

    void like(Long imageId, Long userId);

    void undoLike(Long imageId, Long userId);
}
