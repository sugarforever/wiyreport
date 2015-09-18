package com.wiysoft.report.measurement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by weiliyang on 9/18/15.
 */
@Embeddable
public class ProductPurchaseComboMeasurementPK implements Serializable {
    @Column(nullable = false)
    private Long tid;
    @Column(nullable = false)
    private Long productOid;
    @Column(nullable = false)
    private Long anotherProductOid;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getProductOid() {
        return productOid;
    }

    public void setProductOid(Long productOid) {
        this.productOid = productOid;
    }

    public Long getAnotherProductOid() {
        return anotherProductOid;
    }

    public void setAnotherProductOid(Long anotherProductOid) {
        this.anotherProductOid = anotherProductOid;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(tid).append(productOid).append(anotherProductOid).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ProductPurchaseComboMeasurementPK) {
            final ProductPurchaseComboMeasurementPK that = (ProductPurchaseComboMeasurementPK) obj;
            return new EqualsBuilder().append(this.tid, that.tid).append(this.productOid, that.productOid).append(this.anotherProductOid, that.anotherProductOid).isEquals();
        } else {
            return false;
        }
    }
}
