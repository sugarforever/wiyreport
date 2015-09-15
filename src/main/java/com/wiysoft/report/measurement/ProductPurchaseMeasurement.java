package com.wiysoft.report.measurement;

import com.wiysoft.report.common.CommonUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by weiliyang on 8/25/15.
 */
@Entity
@Table(name = "t_product_purchase", indexes = {@Index(columnList = "sellerId, productNumIid"), @Index(columnList = "sellerId, consumerId")})
public class ProductPurchaseMeasurement {

    @Id
    private long oid;
    private long productNumIid;
    private String productTitle;
    private long sellerId;
    private long consumerId;
    private Date payTime;
    private Float payment;
    private Long number;
    private Float price;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getProductNumIid() {
        return productNumIid;
    }

    public void setProductNumIid(long productNumIid) {
        this.productNumIid = productNumIid;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(long consumerId) {
        this.consumerId = consumerId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
