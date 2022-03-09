package com.example.instagram2.service.serviceImpl;


import com.example.instagram2.entity.Tag;
import com.example.instagram2.repository.TagRepository;
import com.example.instagram2.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <code>TagService</code><br>
 * 태그에 관련된 서비스
 *
 * @author chasw326
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * 태그 리스트를 가져옴
     * @param imageId
     * @return
     */
    @Override
    @Transactional
    public List<Tag> getTags(Long imageId){
        return tagRepository.findTagByImage_InoOrderByTno(imageId);
    }
}
