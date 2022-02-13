package com.example.instagram2.repository;

import com.example.instagram2.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    //필요 없음
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO likes(image, member) VALUES(:imageId, :memberId)", nativeQuery = true)
    void like(Long imageId, Long memberId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Likes l WHERE l.image.ino =:imageId AND l.member.mno =:memberId")
    void unlike(@Param("imageId") Long imageId,@Param("memberId") Long memberId);

    @Transactional
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.image.ino =:imageId")
    Long getLikesCntByImageId(@Param("imageId") Long imageId);

    @Transactional
    @Query("SELECT l.member.mno FROM Likes l WHERE l.image.ino =:imageId")
    List<Long> getMemberIdByImageId(@Param("imageId") Long imageId);

    @Transactional
    @Query("SELECT l.member.email FROM Likes l WHERE l.member.email =:email")
    Object findByEmail(String email);



}
