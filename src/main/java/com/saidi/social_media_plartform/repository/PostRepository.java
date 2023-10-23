package com.saidi.social_media_plartform.repository;

import com.saidi.social_media_plartform.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    //Optional<Posts> findByUserOrContent(String username, String content);
}
