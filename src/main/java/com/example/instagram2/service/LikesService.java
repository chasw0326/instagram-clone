package com.example.instagram2.service;

public interface LikesService {

    void like(Long imageId, Long userId);

    void undoLike(Long imageId, Long userId);
}
