package com.saidi.social_media_plartform.repository;

import com.saidi.social_media_plartform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameOrEmailIgnoreCase(String username, String email);

    @Query(
            "select u from users u where lower(u.username) like %:username%"
    )
    List<User> findByUsernameIgnoreCase(String username);
    User findByUsername(String username);



}
