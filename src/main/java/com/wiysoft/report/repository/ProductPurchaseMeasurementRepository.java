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
import java.util.Date;

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

    @Query("select m.payTime, sum(m.payment), sum(m.number) from ProductPurchaseMeasurement m where m.sellerId = ?1 and m.productNumIid = ?2 and m.payTime >= ?3 and m.payTime < ?4 group by DATE_FORMAT(m.payTime, ?5) order by m.payTime asc")
    public Page findSumPaymentAndCountBySellerIdAndProductNumIidGroupByPayTime(long sellerId, long productNumIid, Date startPayTime, Date endPayTime, String sqlDateFormat, Pageable pageable);

    @Query("select m.productNumIid, sum(m.payment), sum(m.number) from ProductPurchaseMeasurement m where m.sellerId = ?1 and m.productNumIid in ?2 and m.payTime >= ?3 and m.payTime < ?4 group by m.productNumIid")
    public Page findSumPaymentAndCountBySellerIdAndProductNumIidWithinPayTime(long sellerId, Collection productNumIids, Date startPayTime, Date endPayTime, Pageable pageable);
}
