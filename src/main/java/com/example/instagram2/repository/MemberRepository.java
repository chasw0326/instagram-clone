package com.example.instagram2.repository;

import com.example.instagram2.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * roleSet을 @EntityGraph로 조인한 상태로 값들을 가져옴
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email AND m.fromSocial =:fromSocial")
    Optional<Member> findByEmailAndSocial(@Param("email") String email,
                                          @Param("fromSocial") boolean fromSocial);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.username =:username")
    Optional<Member> findByUsername(@Param("username") String username);

    @Query("SELECT m.profileImageUrl ,m.username FROM Member m WHERE m.mno=:mno")
    Object getProfileImagAndUsernameById(@Param("mno") Long mno);

    @Query("SELECT m FROM Member m WHERE m.username LIKE %:keyword% OR m.intro LIKE %:keyword%")
    List<Member> findMembersSearch(@Param("keyword") String keyword);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email")
    Member getByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.username =:username")
    Member getByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email")
    Optional<Member> findByEmail(@Param("email") String email);

    @Query("SELECT m.profileImageUrl FROM Member m WHERE m.mno=:id")
    String getProfileImageById(@Param("id") Long id);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
