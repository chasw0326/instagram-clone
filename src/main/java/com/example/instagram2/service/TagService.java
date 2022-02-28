package com.example.instagram2.service;

import com.example.instagram2.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getTags(Long imageId);
}
