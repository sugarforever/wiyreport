package com.wiysoft.report.repository;

import com.wiysoft.report.measurement.ProductPurchaseMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface ProductPurchaseMeasurementRepository extends JpaRepository<ProductPurchaseMeasurement, Long> {

    @Query("select m from ProductPurchaseMeasurement m where m.oid in ?1")
    public Page<ProductPurchaseMeasurement> findAllByOids(Collection<Long> oids, Pageable pageable);

    public Page<ProductPurchaseMeasurement> findAllByConsumerIdAndProductNumIid(long consumerId, long productNumIid, Pageable pageable);

    @Query("select m.productNumIid, m.productTitle, count(m.oid) as purchased from ProductPurchaseMeasurement m where m.consumerId = ?1 group by m.productNumIid order by purchased desc")
    public Page findAllProductsByConsumerId(long consumerId, Pageable pageable);
}
