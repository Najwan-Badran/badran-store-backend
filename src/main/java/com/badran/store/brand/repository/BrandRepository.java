package com.badran.store.brand.repository;

import com.badran.store.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for brand catalog records.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
