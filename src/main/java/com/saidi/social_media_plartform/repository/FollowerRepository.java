package com.saidi.social_media_plartform.repository;

import com.saidi.social_media_plartform.models.Follows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowerRepository extends JpaRepository<Follows, Long> {
}
