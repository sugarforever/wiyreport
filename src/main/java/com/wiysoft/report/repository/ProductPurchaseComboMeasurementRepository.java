package com.wiysoft.report.repository;

import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.measurement.ProductPurchaseComboMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface ProductPurchaseComboMeasurementRepository extends JpaRepository<ProductPurchaseComboMeasurement, Long> {

    @Query("select m.productNumberIid, m.anotherProductNumberIid, count(distinct m.productPurchaseComboMeasurementPK.tid) from ProductPurchaseComboMeasurement m where m.sellerId=?1 and m.payTime >= ?2 and m.payTime < ?3 group by m.productNumberIid, m.anotherProductNumberIid")
    public Page<Object[]> findProductPurchaseComboAndCountBySellerIdAndPayTime(long sellerId, Date startPayTime, Date endPayTime, Pageable pageable);

    @Query("select m.productNumberIid, m.anotherProductNumberIid, count(distinct m.productPurchaseComboMeasurementPK.tid) from ProductPurchaseComboMeasurement m where m.sellerId=?1 and (m.productNumberIid=?2 or m.anotherProductNumberIid=?2) and m.payTime >= ?3 and m.payTime < ?4 group by m.productNumberIid, m.anotherProductNumberIid")
    public Page<Object[]> findProductPurchaseComboAndCountBySellerIdProductNumberIidAndPayTime(long sellerId, long productNumberIid, Date startPayTime, Date endPayTime, Pageable pageable);

    @Query("select max(m.payTime) from ProductPurchaseComboMeasurement m")
    public Date findMaxPayTime();
}
