package com.badran.store.repository;

import com.badran.store.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for customer delivery addresses.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
