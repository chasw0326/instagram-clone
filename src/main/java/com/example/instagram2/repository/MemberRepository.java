package com.example.instagram2.repository;

import com.example.instagram2.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email AND m.fromSocial =:fromSocial")
    Optional<Member> findByEmailAndSocial(@Param("email") String email,
                                          @Param("fromSocial") boolean fromSocial);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.username =:username")
    Optional<Member> findByUsername(String username);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email")
    Member getByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.phoneNum =:phone")
    Member getByPhoneNum(@Param("phone") String phone);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.username =:username")
    Member getByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Member m WHERE m.email =:email")
    Optional<Member> findByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNum(String phone);

    Boolean existsByUsername(String username);
}
