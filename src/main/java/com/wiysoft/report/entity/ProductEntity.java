package com.wiysoft.report.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by weiliyang on 8/25/15.
 */
@Entity
@Table(name = "t_product", indexes = {@Index(columnList = "numberIid, sellerId")})
public class ProductEntity {

    @Id
    private Long numberIid;

    @Column(nullable = false)
    private Long sellerId;
    @Column
    private String title;
    @Column
    private String picturePath;
    @Column
    private Float price;
    @Column
    private String skuId;
    @Column
    private String outerSkuId;
    @Column
    private String skuPropertiesName;

    public Long getNumberIid() {
        return numberIid;
    }

    public void setNumberIid(Long numberIid) {
        this.numberIid = numberIid;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getOuterSkuId() {
        return outerSkuId;
    }

    public void setOuterSkuId(String outerSkuId) {
        this.outerSkuId = outerSkuId;
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName;
    }
}
