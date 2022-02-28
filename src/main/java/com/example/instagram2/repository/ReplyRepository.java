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

    List<Reply> getRepliesByImageOrderByRegDateAsc(Image image, Pageable pageable);

//    @EntityGraph(attributePaths = {"member.mno", "member.username"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "SELECT * FROM Reply " +
//            "WHERE image =:imageId " +
//            "ORDER BY regDate DESC LIMIT 3",
//            nativeQuery = true)
//    List<Reply> getTop3ByImage_InoOrderByRegDate(@Param("imageId") Long imageId);

    @EntityGraph(attributePaths = {"member.mno", "member.username"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Reply> getTop3ByImage_InoOrderByRegDate(Long imageId);
//    Optional<Reply> findByEmail(String email);
}
