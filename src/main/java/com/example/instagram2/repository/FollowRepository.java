package com.example.instagram2.repository;

import com.example.instagram2.entity.Follow;
import com.example.instagram2.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Transactional
    @Query(value ="SELECT m.mno, m.username, m.profileImageUrl " +
            "FROM Member m " +
            "WHERE m.mno IN " +
            "(SELECT f.fromMember.mno FROM Follow f WHERE f.toMember.mno =:toMemberId )" +
            "ORDER BY m.mno",
            countQuery = "SELECT COUNT(m) FROM Member m")
    Page<Object[]> getFollowerData(@Param("toMemberId") Long toMemberId, Pageable pageable);

    @Transactional
    @Query(value ="SELECT m.mno, m.username, m.profileImageUrl " +
            "FROM Member m " +
            "WHERE m.mno IN " +
            "(SELECT f.toMember.mno FROM Follow f WHERE f.fromMember.mno =:fromMemberId )" +
            "ORDER BY m.mno",
            countQuery = "SELECT COUNT(m) FROM Member m")
    Page<Object[]> getFollowData(@Param("fromMemberId") Long fromMemberId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM Follow f WHERE f.fromMember.mno =:fromMemberId AND f.toMember.mno =:toMemberId")
    void unFollow(@Param("fromMemberId") Long fromMemberId, @Param("toMemberId") Long toMemberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.fromMember.mno =:fromMemberId AND f.toMember.mno =:toMemberId")
    int followState(@Param("fromMemberId") Long fromMemberId, @Param("toMemberId") Long toMemberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.toMember.mno =:toMemberId")
    Long getFollowerCount(@Param("toMemberId") Long toMemberId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.fromMember.mno =:fromMemberId")
    Long getFollowCount(@Param("fromMemberId") Long fromMemberId);

    //필요 없음
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO follow(from_Member, to_Member) VALUES(:fromMemberId, :toMemberId)", nativeQuery = true)
    void follow(@Param("fromMemberId") Long fromMemberId, @Param("toMemberId") Long toMemberId);

    Boolean existsByFromMember(Member fromMember);

    Boolean existsByToMember(Member toMember);

}
