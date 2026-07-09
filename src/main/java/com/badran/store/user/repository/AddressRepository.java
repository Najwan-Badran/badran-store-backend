package com.badran.store.user.repository;

import com.badran.store.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for customer delivery addresses.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    /**
     * Finds saved addresses for a user.
     */
    List<Address> findByUserUserId(Long userId);
}
