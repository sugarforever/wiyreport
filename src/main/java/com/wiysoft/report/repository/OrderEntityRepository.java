package com.wiysoft.report.repository;

import com.wiysoft.report.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    public OrderEntity findOneByNumberIid(long numberIid);
}
