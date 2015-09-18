package com.wiysoft.report.repository;

import com.wiysoft.report.entity.ConsumerEntity;
import com.wiysoft.report.entity.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * Created by weiliyang on 7/24/15.
 */
@Transactional
@Repository
public interface ConsumerEntityRepository extends JpaRepository<ConsumerEntity, Long> {

    @Query("select c.consumerNick from ConsumerEntity c where c.sellerId = ?1 and c.consumerNickCrc32 in ?2")
    public List findAllConsumerNicksByCRC32(long sellerId, Collection crc32s, Pageable pageable);

    @Query("select c from ConsumerEntity c where c.sellerId = ?1 and c.consumerNickCrc32 in ?2")
    public Page<ConsumerEntity> findAllConsumerEntitiesByCRC32(long sellerId, Collection crc32s, Pageable pageable);

    @Query("select c from ConsumerEntity c where c.sellerId = ?1 order by c.countOfBills desc")
    public Page<ConsumerEntity> findAllConsumerEntitiesOrderByCountOfBills(long sellerId, Pageable pageable);

    public List findAllBySellerId(long sellerId, Pageable pageable);

    public ConsumerEntity findOneByConsumerNickCrc32AndConsumerNick(long consumerNickCrc32, String consumerNick);
}
