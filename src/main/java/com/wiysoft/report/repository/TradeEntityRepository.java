package com.wiysoft.report.repository;

import com.wiysoft.report.entity.TradeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
public interface TradeEntityRepository extends JpaRepository<TradeEntity, Long> {

    @Query("select MIN(t.created) from TradeEntity t where t.sellerId = ?1 and t.status not in ?2")
    public Date findMinCreatedBySellerIdAndNotStatus(long sellerId, List<String> status);

    @Query("select MIN(t.created), SUM(t.totalFee) from TradeEntity t where t.sellerId = ?1 and t.status in ?2 and t.created >= ?3 and t.created < ?4 group by DATE_FORMAT(t.created, ?5)")
    public Collection findSumTotalFeeBySellerIdStatus(long sellerId, List<String> status, Date startCreated, Date endCreated, String dateFormat);

    @Query("select MIN(t.created), SUM(t.totalFee) from TradeEntity t where t.sellerId = ?1 and t.status not in ?2 and t.created >= ?3 and t.created < ?4 group by DATE_FORMAT(t.created, ?5)")
    public Collection findSumTotalFeeBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String dateFormat);

    @Query("select MIN(t.created), COUNT(t.tid) from TradeEntity t where t.sellerId = ?1 and t.status in ?2 and t.created >= ?3 and t.created < ?4 group by DATE_FORMAT(t.created, ?5)")
    public Collection findCountOfTradesBySellerIdStatus(long sellerId, List<String> status, Date startCreated, Date endCreated, String dateFormat);

    @Query("select MIN(t.created), COUNT(t.tid) from TradeEntity t where t.sellerId = ?1 and t.status not in ?2 and t.created >= ?3 and t.created < ?4 group by DATE_FORMAT(t.created, ?5)")
    public Collection findCountOfTradesBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String dateFormat);

    @Query("select t from TradeEntity t where t.tid in ?1")
    public Collection findTradeEntitiesByTids(List tids);

    @Query("select distinct(t.buyerNick) from TradeEntity t where t.sellerId = ?1")
    public List findAllDistinctConsumerNicks(long sellerId, Pageable pageable);

    @Query("select t.buyerNick, MIN(t.payTime), MAX(t.payTime), COUNT(t.tid) from TradeEntity t where t.sellerId = ?1 group by t.buyerNickCrc32, t.buyerNick")
    public Page<Object[]> findAllConsumersIndex(long sellerId, Pageable pageable);

    @Modifying
    @Query("update TradeEntity t set t.buyerNickCrc32=CRC32(t.buyerNick)")
    public void updateTradeEntitiesBuyerNickCrc32();

    @Query("select t from TradeEntity t where t.sellerId = ?1 and t.payTime >= ?2 and t.payTime < ?3")
    public Page<TradeEntity> findAllBySellerIdAndPayTime(long sellerId, Date startPayTime, Date endPayTime, Pageable pageable);

    @Query("select t from TradeEntity t where t.payTime >= ?1 and t.payTime < ?2")
    public Page<TradeEntity> findAllByPayTime(Date startPayTime, Date endPayTime, Pageable pageable);
}
