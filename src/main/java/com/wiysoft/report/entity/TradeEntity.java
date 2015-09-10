package com.wiysoft.report.entity;

import com.wiysoft.report.common.CommonUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.util.*;
import java.util.zip.CRC32;

/**
 * Created by weiliyang on 8/25/15.
 */
@Entity
@Table(name = "t_trade", indexes = {@Index(columnList = "created"), @Index(columnList = "payTime")})
public class TradeEntity {

    @Id
    private Long tid;

    @Column
    private String sellerNick;
    @Column
    private long sellerId;
    @Column
    private String picPath;
    @Column
    private Float payment;
    @Column
    private Boolean sellerRate;
    @Column
    private Float postFee;
    @Column
    private String receiverName;
    @Column
    private String receiverState;
    @Column
    private String receiverAddress;
    @Column
    private String receiverZip;
    @Column
    private String receiverMobile;
    @Column
    private String receiverPhone;
    @Column
    private String receiverCountry;
    @Column
    private String receiverCity;
    @Column
    private String receiverTown;
    @Column
    private String receiverDistrict;
    @Column
    private String orderTaxFee;
    @Column
    private Long number;
    @Column
    private Long numberIid;
    @Column
    private String status;
    @Column
    private String title;
    @Column
    private String type;
    @Column
    private Float price;
    @Column
    private Float discountFee;
    @Column
    private Float totalFee;
    @Column
    private Date created;
    @Column
    private Date payTime;
    @Column
    private Date modified;
    @Column
    private Date endTime;
    @Column
    private Date consignTime;
    @Column
    private Integer sellerFlag;
    @Column
    private String buyerNick;
    @Column
    private long buyerNickCrc32;
    @Column
    private Boolean hasBuyerMessage;
    @Column
    private Float creditCardFee;
    @Column
    private String markDesc;
    @Column
    private String shippingType;
    @Column
    private Float adjustFee;
    @Column
    private String tradeFrom;
    @Column
    private Boolean buyerRate;

    @OneToMany(targetEntity = OrderEntity.class, cascade = CascadeType.ALL)
    private List<OrderEntity> orderEntities;

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Boolean isSellerRate() {
        return sellerRate;
    }

    public Float getPostFee() {
        return postFee;
    }

    public void setPostFee(Float postFee) {
        this.postFee = postFee;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverTown() {
        return receiverTown;
    }

    public void setReceiverTown(String receiverTown) {
        this.receiverTown = receiverTown;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getOrderTaxFee() {
        return orderTaxFee;
    }

    public void setOrderTaxFee(String orderTaxFee) {
        this.orderTaxFee = orderTaxFee;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Float discountFee) {
        this.discountFee = discountFee;
    }

    public Float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Float totalFee) {
        this.totalFee = totalFee;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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

    public Integer getSellerFlag() {
        return sellerFlag;
    }

    public void setSellerFlag(Integer sellerFlag) {
        this.sellerFlag = sellerFlag;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
        this.buyerNickCrc32 = CommonUtils.getCRC32(this.buyerNick);
    }

    public long getBuyerNickCrc32() {
        return buyerNickCrc32;
    }

    public void setBuyerNickCrc32(long buyerNickCrc32) {
        this.buyerNickCrc32 = buyerNickCrc32;
    }

    public Boolean isHasBuyerMessage() {
        return hasBuyerMessage;
    }

    public Float getCreditCardFee() {
        return creditCardFee;
    }

    public void setCreditCardFee(Float creditCardFee) {
        this.creditCardFee = creditCardFee;
    }

    public String getMarkDesc() {
        return markDesc;
    }

    public void setMarkDesc(String markDesc) {
        this.markDesc = markDesc;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public Float getAdjustFee() {
        return adjustFee;
    }

    public void setAdjustFee(Float adjustFee) {
        this.adjustFee = adjustFee;
    }

    public String getTradeFrom() {
        return tradeFrom;
    }

    public void setTradeFrom(String tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public Boolean isBuyerRate() {
        return buyerRate;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public void appendOrderEntity(OrderEntity orderEntity) {
        if (this.orderEntities == null) {
            this.orderEntities = new ArrayList<OrderEntity>();
        }

        this.orderEntities.add(orderEntity);
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public Boolean getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public Boolean getHasBuyerMessage() {
        return hasBuyerMessage;
    }

    public void setHasBuyerMessage(Boolean hasBuyerMessage) {
        this.hasBuyerMessage = hasBuyerMessage;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Map<Long, OrderEntity> getOrderEntityMap() {
        Map<Long, OrderEntity> map = new HashMap<Long, OrderEntity>();
        if (this.orderEntities != null) {
            for (OrderEntity o : this.orderEntities) {
                map.put(o.getOid(), o);
            }
        }
        return map;
    }

    public void removeOrderEntitiesBy(List<Long> oids) {
        if (this.orderEntities != null && oids != null && oids.size() > 0) {
            for (int i = this.orderEntities.size() - 1; i >= 0; --i) {
                OrderEntity o = this.orderEntities.get(i);
                if (oids.contains(o.getOid())) {
                    this.orderEntities.remove(i);
                }
            }
        }
    }
}
