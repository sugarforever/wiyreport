package com.wiysoft.report.common;

import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.wiysoft.report.entity.OrderEntity;
import com.wiysoft.report.entity.TradeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by weiliyang on 8/25/15.
 */
public final class EntityBuilder {

    public static OrderEntity buildOrderEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity e = new OrderEntity();
        e.setOid(order.getOid());
        e.setItemMealName(order.getItemMealName());
        e.setItemMealId(order.getItemMealId());
        e.setOuterIid(order.getOuterIid());
        e.setCid(order.getCid());
        e.setSubOrderTaxFee(order.getSubOrderTaxFee());
        e.setSubOrderTaxRate(order.getSubOrderTaxRate());
        e.setSkuId(order.getSkuId());
        e.setOuterSkuId(order.getOuterSkuId());
        e.setSkuPropertiesName(order.getSkuPropertiesName());
        e.setBuyerRate(order.getBuyerRate());
        e.setSellerRate(order.getSellerRate());
        e.setSellerType(order.getSellerType());
        e.setNumber(order.getNum());
        e.setNumberIid(order.getNumIid());
        e.setTotalFee(CommonUtils.parseFloat(order.getTotalFee(), null));
        e.setPayment(CommonUtils.parseFloat(order.getPayment(), null));
        e.setDiscountFee(CommonUtils.parseFloat(order.getDiscountFee(), null));
        e.setAdjustFee(CommonUtils.parseFloat(order.getAdjustFee(), null));
        e.setDivideOrderFee(CommonUtils.parseFloat(order.getDivideOrderFee(), null));
        e.setPartMjzDiscount(CommonUtils.parseFloat(order.getPartMjzDiscount(), null));
        e.setPicturePath(order.getPicPath());
        e.setPrice(CommonUtils.parseFloat(order.getPrice(), null));
        e.setRefundId(order.getRefundId());
        e.setRefundStatus(order.getRefundStatus());
        e.setStatus(order.getStatus());
        e.setTitle(order.getTitle());
        e.setEndTime(order.getEndTime());
        e.setConsignTime(CommonUtils.parseStrToDate(order.getConsignTime(), "yyyy-MM-dd HH:mm:ss"));
        e.setShippingType(order.getShippingType());
        e.setBindOid(order.getBindOid());
        e.setLogisticsCompany(order.getLogisticsCompany());
        e.setInvoiceNo(order.getInvoiceNo());
        e.setDaiXiao(order.getIsDaixiao());
        e.setTicketOuterId(order.getTicketOuterId());
        e.setTicketExpdateKey(order.getTicketExpdateKey());
        e.setStoreCode(order.getStoreCode());
        e.setWww(order.getIsWww());
        e.setTmserSpuCode(order.getTmserSpuCode());
        return e;
    }

    public static TradeEntity buildTradeEntity(Trade trade, long sellerId) {
        if (trade == null) {
            return null;
        }

        TradeEntity entity = new TradeEntity();
        entity.setSellerId(sellerId);
        entity.setTid(trade.getTid());
        entity.setSellerNick(trade.getSellerNick());
        entity.setPicPath(trade.getPicPath());
        entity.setPayment(CommonUtils.parseFloat(trade.getPayment(), null));
        entity.setSellerRate(trade.getSellerRate());
        entity.setPostFee(CommonUtils.parseFloat(trade.getPostFee(), null));
        entity.setReceiverName(trade.getReceiverName());
        entity.setReceiverState(trade.getReceiverState());
        entity.setReceiverAddress(trade.getReceiverAddress());
        entity.setReceiverZip(trade.getReceiverZip());
        entity.setReceiverMobile(trade.getReceiverMobile());
        entity.setReceiverPhone(trade.getReceiverPhone());
        entity.setReceiverCountry(trade.getReceiverCountry());
        entity.setReceiverCity(trade.getReceiverCity());
        entity.setReceiverTown(trade.getReceiverTown());
        entity.setReceiverDistrict(trade.getReceiverDistrict());
        entity.setOrderTaxFee(trade.getOrderTaxFee());
        entity.setNumber(trade.getNum());
        entity.setNumberIid(trade.getNumIid());
        entity.setStatus(trade.getStatus());
        entity.setTitle(trade.getTitle());
        entity.setType(trade.getType());
        entity.setPrice(CommonUtils.parseFloat(trade.getPrice(), null));
        entity.setDiscountFee(CommonUtils.parseFloat(trade.getDiscountFee(), null));
        entity.setTotalFee(CommonUtils.parseFloat(trade.getTotalFee(), null));
        entity.setCreated(trade.getCreated());
        entity.setPayTime(trade.getPayTime());
        entity.setModified(trade.getModified());
        entity.setEndTime(trade.getEndTime());
        entity.setConsignTime(trade.getConsignTime());
        entity.setSellerFlag(trade.getSellerFlag() == null ? null : trade.getSellerFlag().intValue());
        entity.setBuyerNick(trade.getBuyerNick());
        entity.setHasBuyerMessage(trade.getHasBuyerMessage());
        entity.setCreditCardFee(CommonUtils.parseFloat(trade.getCreditCardFee(), null));
        entity.setMarkDesc(trade.getMarkDesc());
        entity.setShippingType(trade.getShippingType());
        entity.setAdjustFee(CommonUtils.parseFloat(trade.getAdjustFee(), null));
        entity.setTradeFrom(trade.getTradeFrom());
        entity.setBuyerRate(trade.getBuyerRate());

        List<Order> orders = trade.getOrders();
        if (orders != null) {
            for (Order order : orders) {
                entity.appendOrderEntity(buildOrderEntity(order));
            }
        }

        return entity;
    }

    public static void updateTradeEntity(TradeEntity updatee, TradeEntity updater) {
        if (updatee == null || updater == null || updatee.getTid() == null || updater.getTitle() == null || updatee.getTid() != updater.getTid()) {
            return;
        }

        updatee.setSellerId(updater.getSellerId());
        updatee.setSellerNick(updater.getSellerNick());
        updatee.setPicPath(updater.getPicPath());
        updatee.setPayment(updater.getPayment());
        updatee.setSellerRate(updater.getSellerRate());
        updatee.setPostFee(updater.getPostFee());
        updatee.setReceiverName(updater.getReceiverName());
        updatee.setReceiverState(updater.getReceiverState());
        updatee.setReceiverAddress(updater.getReceiverAddress());
        updatee.setReceiverZip(updater.getReceiverZip());
        updatee.setReceiverMobile(updater.getReceiverMobile());
        updatee.setReceiverPhone(updater.getReceiverPhone());
        updatee.setReceiverCountry(updater.getReceiverCountry());
        updatee.setReceiverCity(updater.getReceiverCity());
        updatee.setReceiverTown(updater.getReceiverTown());
        updatee.setReceiverDistrict(updater.getReceiverDistrict());
        updatee.setOrderTaxFee(updater.getOrderTaxFee());
        updatee.setNumber(updater.getNumber());
        updatee.setNumberIid(updater.getNumberIid());
        updatee.setStatus(updater.getStatus());
        updatee.setTitle(updater.getTitle());
        updatee.setType(updater.getType());
        updatee.setPrice(updater.getPrice());
        updatee.setDiscountFee(updater.getDiscountFee());
        updatee.setTotalFee(updater.getTotalFee());
        updatee.setCreated(updater.getCreated());
        updatee.setPayTime(updater.getPayTime());
        updatee.setModified(updater.getModified());
        updatee.setEndTime(updater.getEndTime());
        updatee.setConsignTime(updater.getConsignTime());
        updatee.setSellerFlag(updater.getSellerFlag());
        updatee.setBuyerNick(updater.getBuyerNick());
        updatee.setHasBuyerMessage(updater.getHasBuyerMessage());
        updatee.setCreditCardFee(updater.getCreditCardFee());
        updatee.setMarkDesc(updater.getMarkDesc());
        updatee.setShippingType(updater.getShippingType());
        updatee.setAdjustFee(updater.getAdjustFee());
        updatee.setTradeFrom(updater.getTradeFrom());
        updatee.setBuyerRate(updater.getBuyerRate());

        List<Long> existedOids = new ArrayList<Long>(updatee.getOrderEntityMap().keySet());
        Set<Long> newOids = updater.getOrderEntityMap().keySet();
        List<Long> oidsToBeDeleted = new ArrayList<Long>();
        for (int i = existedOids.size() - 1; i >= 0; --i) {
            Long existedOid = existedOids.get(i);
            if (!newOids.contains(existedOid)) {
                oidsToBeDeleted.add(existedOid);
            }
        }
        updatee.removeOrderEntitiesBy(oidsToBeDeleted);

        Map<Long, OrderEntity> existedOrdersMap = updatee.getOrderEntityMap();
        Map<Long, OrderEntity> newOrdersMap = updater.getOrderEntityMap();

        for (Long oid : newOrdersMap.keySet()) {
            OrderEntity newOrderEntity = newOrdersMap.get(oid);
            if (existedOrdersMap.containsKey(oid)) {
                // update
                OrderEntity existedOrderEntity = existedOrdersMap.get(oid);

                existedOrderEntity.setItemMealName(newOrderEntity.getItemMealName());
                existedOrderEntity.setItemMealId(newOrderEntity.getItemMealId());
                existedOrderEntity.setOuterIid(newOrderEntity.getOuterIid());
                existedOrderEntity.setCid(newOrderEntity.getCid());
                existedOrderEntity.setSubOrderTaxFee(newOrderEntity.getSubOrderTaxFee());
                existedOrderEntity.setSubOrderTaxRate(newOrderEntity.getSubOrderTaxRate());
                existedOrderEntity.setSkuId(newOrderEntity.getSkuId());
                existedOrderEntity.setOuterSkuId(newOrderEntity.getOuterSkuId());
                existedOrderEntity.setSkuPropertiesName(newOrderEntity.getSkuPropertiesName());
                existedOrderEntity.setBuyerRate(newOrderEntity.getBuyerRate());
                existedOrderEntity.setSellerRate(newOrderEntity.getSellerRate());
                existedOrderEntity.setSellerType(newOrderEntity.getSellerType());
                existedOrderEntity.setNumber(newOrderEntity.getNumber());
                existedOrderEntity.setNumberIid(newOrderEntity.getNumberIid());
                existedOrderEntity.setTotalFee(newOrderEntity.getTotalFee());
                existedOrderEntity.setPayment(newOrderEntity.getPayment());
                existedOrderEntity.setDiscountFee(newOrderEntity.getDiscountFee());
                existedOrderEntity.setAdjustFee(newOrderEntity.getAdjustFee());
                existedOrderEntity.setDivideOrderFee(newOrderEntity.getDivideOrderFee());
                existedOrderEntity.setPartMjzDiscount(newOrderEntity.getPartMjzDiscount());
                existedOrderEntity.setPicturePath(newOrderEntity.getPicturePath());
                existedOrderEntity.setPrice(newOrderEntity.getPrice());
                existedOrderEntity.setRefundId(newOrderEntity.getRefundId());
                existedOrderEntity.setRefundStatus(newOrderEntity.getRefundStatus());
                existedOrderEntity.setStatus(newOrderEntity.getStatus());
                existedOrderEntity.setTitle(newOrderEntity.getTitle());
                existedOrderEntity.setEndTime(newOrderEntity.getEndTime());
                existedOrderEntity.setConsignTime(newOrderEntity.getConsignTime());
                existedOrderEntity.setShippingType(newOrderEntity.getShippingType());
                existedOrderEntity.setBindOid(newOrderEntity.getBindOid());
                existedOrderEntity.setLogisticsCompany(newOrderEntity.getLogisticsCompany());
                existedOrderEntity.setInvoiceNo(newOrderEntity.getInvoiceNo());
                existedOrderEntity.setDaiXiao(newOrderEntity.getDaiXiao());
                existedOrderEntity.setTicketOuterId(newOrderEntity.getTicketOuterId());
                existedOrderEntity.setTicketExpdateKey(newOrderEntity.getTicketExpdateKey());
                existedOrderEntity.setStoreCode(newOrderEntity.getStoreCode());
                existedOrderEntity.setWww(newOrderEntity.isWww());
                existedOrderEntity.setTmserSpuCode(newOrderEntity.getTmserSpuCode());
            } else {
                // append
                updatee.appendOrderEntity(newOrderEntity);
            }
        }
    }
}
