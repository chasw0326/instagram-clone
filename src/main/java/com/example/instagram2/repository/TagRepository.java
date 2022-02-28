package com.example.instagram2.repository;

import com.example.instagram2.entity.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TagRepository extends JpaRepository<Tag, Long> {

    @EntityGraph(attributePaths = {"image"}, type= EntityGraph.EntityGraphType.LOAD)
    List<Tag> findTagByImage_InoOrderByTno(Long imageId);
}
