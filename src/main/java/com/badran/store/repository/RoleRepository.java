package com.badran.store.repository;

import com.badran.store.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for security role lookup.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Finds a role by its unique role name.
     */
    Optional<Role> findByRoleName(String roleName);
}
