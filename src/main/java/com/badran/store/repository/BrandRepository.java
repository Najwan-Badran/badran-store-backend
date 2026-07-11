package com.badran.store.repository;

import com.badran.store.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for brand catalog records.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
