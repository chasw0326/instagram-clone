package com.example.instagram2.repository;

import com.example.instagram2.entity.Image;
import com.example.instagram2.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> getRepliesByImageOrderByRegDateAsc(Image image, Pageable pageable);

//    Optional<Reply> findByEmail(String email);
}
