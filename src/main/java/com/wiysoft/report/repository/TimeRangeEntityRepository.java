package com.wiysoft.report.repository;

import com.wiysoft.report.entity.ConsumerEntity;
import com.wiysoft.report.entity.TimeRangeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface TimeRangeEntityRepository extends JpaRepository<TimeRangeEntity, Long> {

    //public Page<TimeRangeEntity> findAllBySellerIdOrderByStartDate(long sellerId, Pageable pageable);
}
