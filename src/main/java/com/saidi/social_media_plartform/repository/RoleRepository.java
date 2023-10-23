package com.saidi.social_media_plartform.repository;

import com.saidi.social_media_plartform.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String name);
}
