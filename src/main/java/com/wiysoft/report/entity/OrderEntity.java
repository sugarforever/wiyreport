package com.wiysoft.report.entity;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by weiliyang on 8/25/15.
 */
@Entity
@Table(name = "t_order", indexes = {@Index(columnList = "numberIid")})
public class OrderEntity {

    @Id
    private Long oid;

    @Column
    private String itemMealName;
    @Column
    private Long itemMealId;
    @Column
    private String outerIid;
    @Column
    private Long cid;
    @Column
    private String subOrderTaxFee;
    @Column
    private String subOrderTaxRate;
    @Column
    private String skuId;
    @Column
    private String outerSkuId;
    @Column
    private String skuPropertiesName;

    @Column
    private Boolean buyerRate;
    @Column
    private Boolean sellerRate;
    @Column
    private String sellerType;

    @Column
    private Long number;
    @Column
    private Long numberIid;
    @Column
    private Float totalFee;
    @Column
    private Float payment;
    @Column
    private Float discountFee;
    @Column
    private Float adjustFee;
    @Column
    private Float divideOrderFee;
    @Column
    private Float partMjzDiscount;

    @Column
    private String picturePath;
    @Column
    private Float price;

    @Column
    private Long refundId;
    @Column
    private String refundStatus;
    @Column
    private String status;
    @Column
    private String title;

    @Column
    private Date endTime;
    @Column
    private Date consignTime;
    @Column
    private String shippingType;
    @Column
    private Long bindOid;
    @Column
    private String logisticsCompany;
    @Column
    private String invoiceNo;
    @Column
    private Boolean daiXiao;
    @Column
    private String ticketOuterId;
    @Column
    private String ticketExpdateKey;
    @Column
    private String storeCode;
    @Column
    private Boolean www;
    @Column
    private String tmserSpuCode;

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getItemMealName() {
        return itemMealName;
    }

    public void setItemMealName(String itemMealName) {
        this.itemMealName = itemMealName;
    }

    public Long getItemMealId() {
        return itemMealId;
    }

    public void setItemMealId(Long itemMealId) {
        this.itemMealId = itemMealId;
    }

    public String getOuterIid() {
        return outerIid;
    }

    public void setOuterIid(String outerIid) {
        this.outerIid = outerIid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getSubOrderTaxFee() {
        return subOrderTaxFee;
    }

    public void setSubOrderTaxFee(String subOrderTaxFee) {
        this.subOrderTaxFee = subOrderTaxFee;
    }

    public String getSubOrderTaxRate() {
        return subOrderTaxRate;
    }

    public void setSubOrderTaxRate(String subOrderTaxRate) {
        this.subOrderTaxRate = subOrderTaxRate;
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

    public Boolean isBuyerRate() {
        return buyerRate;
    }

    public Boolean isSellerRate() {
        return sellerRate;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumberIid() {
        return numberIid;
    }

    public void setNumberIid(Long numberIid) {
        this.numberIid = numberIid;
    }

    public Float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Float totalFee) {
        this.totalFee = totalFee;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Float getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Float discountFee) {
        this.discountFee = discountFee;
    }

    public Float getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Float adjustFee) {
        this.adjustFee = adjustFee;
    }

    public Float getDivideOrderFee() {
        return divideOrderFee;
    }

    public void setDivideOrderFee(Float divideOrderFee) {
        this.divideOrderFee = divideOrderFee;
    }

    public Float getPartMjzDiscount() {
        return partMjzDiscount;
    }

    public void setPartMjzDiscount(Float partMjzDiscount) {
        this.partMjzDiscount = partMjzDiscount;
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

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Long getBindOid() {
        return bindOid;
    }

    public void setBindOid(Long bindOid) {
        this.bindOid = bindOid;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Boolean isDaiXiao() {
        return daiXiao;
    }

    public String getTicketOuterId() {
        return ticketOuterId;
    }

    public void setTicketOuterId(String ticketOuterId) {
        this.ticketOuterId = ticketOuterId;
    }

    public String getTicketExpdateKey() {
        return ticketExpdateKey;
    }

    public void setTicketExpdateKey(String ticketExpdateKey) {
        this.ticketExpdateKey = ticketExpdateKey;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Boolean isWww() {
        return www;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Boolean getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public Boolean getDaiXiao() {
        return daiXiao;
    }

    public void setDaiXiao(Boolean daiXiao) {
        this.daiXiao = daiXiao;
    }

    public Boolean getWww() {
        return www;
    }

    public void setWww(Boolean www) {
        this.www = www;
    }

    public String getTmserSpuCode() {
        return tmserSpuCode;
    }

    public void setTmserSpuCode(String tmserSpuCode) {
        this.tmserSpuCode = tmserSpuCode;
    }

    public static final class OrderEntityNumberIidAscComparator implements Comparator<OrderEntity> {

        @Override
        public int compare(OrderEntity o1, OrderEntity o2) {
            if (o1 == null || o1.getNumberIid() == null) {
                return -1;
            }

            if (o2 == null || o2.getNumberIid() == null) {
                return 1;
            }

            return o1.getNumberIid().compareTo(o2.getNumberIid());
        }
    }
}
