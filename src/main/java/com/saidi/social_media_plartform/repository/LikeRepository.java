package com.saidi.social_media_plartform.repository;

import com.saidi.social_media_plartform.models.Likes;
import com.saidi.social_media_plartform.models.Posts;
import com.saidi.social_media_plartform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByPostAndLiker(Posts post, User liker);
}
