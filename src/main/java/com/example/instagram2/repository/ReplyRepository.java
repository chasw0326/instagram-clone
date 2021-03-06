package com.example.instagram2.repository;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> getRepliesByImage_InoOrderByRegDateAsc(Long ino, Pageable pageable);

    @EntityGraph(attributePaths = {"member.mno", "member.username"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Reply> getTop3ByImage_InoOrderByRegDate(Long imageId);

}
