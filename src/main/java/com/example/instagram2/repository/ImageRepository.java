package com.example.instagram2.repository;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Long> {

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT i FROM Image i WHERE i.member.mno " +
            "IN (SELECT f.toMember.mno FROM Follow f WHERE f.fromMember.mno = :userId) " +
            "ORDER BY i.regDate DESC")
    Page<Image> getFollowFeed(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Image i LEFT JOIN Tag t ON t.image.ino = i.ino WHERE t.name LIKE %:keyword%")
    Long findTagsSearch(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM Image " +
            "WHERE member =:memberId " +
            "ORDER BY like_Cnt DESC LIMIT 3",
            nativeQuery = true)
    List<Image> getPopularPictureList(@Param("memberId") Long memberId);

    @Query("SELECT i.ImageUrl FROM Image i WHERE i.member.mno =:memberId order by i.regDate")
    List<String> getImageUrlList(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(i) FROM Image i WHERE i.member.mno =:memberId")
    Long getImageCount(@Param("memberId") Long memberId);

}


