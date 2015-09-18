package com.wiysoft.report.measurement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by weiliyang on 9/17/15.
 */
@Entity
@Table(name = "t_product_purchase_combo", indexes = {@Index(columnList = "sellerId"), @Index(columnList = "sellerId, payTime")})
public class ProductPurchaseComboMeasurement implements Serializable {

    @EmbeddedId
    private ProductPurchaseComboMeasurementPK productPurchaseComboMeasurementPK;
    @Column(nullable = false)
    private Long sellerId;
    @Column(nullable = false)
    private Long productNumberIid;
    @Column(nullable = false)
    private Long anotherProductNumberIid;
    @Column(nullable = false)
    private Date payTime;

    public ProductPurchaseComboMeasurementPK getProductPurchaseComboMeasurementPK() {
        return productPurchaseComboMeasurementPK;
    }

    public void setProductPurchaseComboMeasurementPK(ProductPurchaseComboMeasurementPK productPurchaseComboMeasurementPK) {
        this.productPurchaseComboMeasurementPK = productPurchaseComboMeasurementPK;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getProductNumberIid() {
        return productNumberIid;
    }

    public void setProductNumberIid(Long productNumberIid) {
        this.productNumberIid = productNumberIid;
    }

    public Long getAnotherProductNumberIid() {
        return anotherProductNumberIid;
    }

    public void setAnotherProductNumberIid(Long anotherProductNumberIid) {
        this.anotherProductNumberIid = anotherProductNumberIid;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
