package com.wiysoft.report.entity;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by weiliyang on 8/25/15.
 */
@Entity
@Table(name = "t_consumer", indexes = {@Index(columnList = "consumerNickCrc32, consumerNick")})
public class ConsumerEntity {

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getConsumerNickCrc32() {
        return consumerNickCrc32;
    }

    public void setConsumerNickCrc32(Long consumerNickCrc32) {
        this.consumerNickCrc32 = consumerNickCrc32;
    }

    public String getConsumerNick() {
        return consumerNick;
    }

    public void setConsumerNick(String consumerNick) {
        this.consumerNick = consumerNick;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFirstPaid() {
        return firstPaid;
    }

    public void setFirstPaid(Date firstPaid) {
        this.firstPaid = firstPaid;
    }

    public Date getLatestPaid() {
        return latestPaid;
    }

    public void setLatestPaid(Date latestPaid) {
        this.latestPaid = latestPaid;
    }

    public long getCountOfBills() {
        return countOfBills;
    }

    public void setCountOfBills(long countOfBills) {
        this.countOfBills = countOfBills;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;
    private Long consumerNickCrc32;
    private String consumerNick;
    private Date firstPaid;
    private Date latestPaid;
    private long countOfBills;

    public String getStrFirstPaid() {
        String str = CommonUtils.parseStrFromDate(firstPaid, "yyyy-MM-dd HH:mm:ss");
        return str == null ? "" : str;
    }

    public String getStrLatestPaid() {
        String str = CommonUtils.parseStrFromDate(latestPaid, "yyyy-MM-dd HH:mm:ss");
        return str == null ? "" : str;
    }
}
