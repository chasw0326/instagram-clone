package com.example.instagram2.service;

import com.example.instagram2.entity.Tag;

import java.util.List;

/**<strong>구현클래스에 문서화</strong><br>
 * {@link com.example.instagram2.service.serviceImpl.TagServiceImpl}
 */
public interface TagService {

    List<Tag> getTags(Long imageId);
}
