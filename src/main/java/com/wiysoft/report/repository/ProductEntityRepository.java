package com.wiysoft.report.repository;

import com.wiysoft.report.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM ProductEntity p WHERE p.numberIid in ?1")
    public Page<ProductEntity> findAllByNumberIids(Set numberIids, Pageable pageable);

    public Page<ProductEntity> findAllBySellerId(Long sellerId, Pageable pageable);
}
