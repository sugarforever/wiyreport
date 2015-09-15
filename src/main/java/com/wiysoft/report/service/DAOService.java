package com.wiysoft.report.service;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.entity.*;
import com.wiysoft.report.measurement.ProductPurchaseMeasurement;
import com.wiysoft.report.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by weiliyang on 8/24/15.
 */
@Service
public class DAOService {

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private ConsumerEntityRepository consumerEntityRepository;
    @Autowired
    private ProductPurchaseMeasurementRepository productPurchaseMeasurementRepository;
    @Autowired
    private ProductEntityRepository productEntityRepository;

    @Transactional
    public synchronized Visitor insertOrUpdateVisitor(Visitor visitor) {
        if (visitor == null) {
            return null;
        }

        Visitor existed = visitorRepository.findOne(visitor.getVisitorId());
        Visitor returned = null;
        if (existed == null) {
            returned = visitorRepository.save(visitor);
        } else {
            existed.setExpiresIn(visitor.getExpiresIn());
            existed.setR1ExpiresIn(visitor.getR1ExpiresIn());
            existed.setR2ExpiresIn(visitor.getR2ExpiresIn());
            existed.setReExpiresIn(visitor.getReExpiresIn());
            existed.setRefreshToken(visitor.getRefreshToken());
            existed.setTs(visitor.getTs());
            existed.setVisitorNick(visitor.getVisitorNick());
            existed.setW1ExpiresIn(visitor.getW1ExpiresIn());
            existed.setW2ExpiresIn(visitor.getW2ExpiresIn());
            existed.setSessionKey(visitor.getSessionKey());

            returned = visitorRepository.save(existed);
        }

        return returned;
    }

    public void buildConsumersBy(long sellerId) {
        Pageable pageRequest = new PageRequest(0, 500);
        while (true) {
            List consumersIndex = tradeEntityRepository.findAllConsumersIndex(sellerId, pageRequest);

            Hashtable<String, ConsumerEntity> newConsumersHash = new Hashtable<String, ConsumerEntity>();
            List<Long> crc32s = new ArrayList<Long>();
            for (Object consumerIndex : consumersIndex) {
                String consumerNick = (String) ((Object[]) consumerIndex)[0];
                Date firstPaid = (Date) ((Object[]) consumerIndex)[1];
                Date latestPaid = (Date) ((Object[]) consumerIndex)[2];
                Long countOfBills = (Long) ((Object[]) consumerIndex)[3];
                long crc32 = CommonUtils.getCRC32(consumerNick);

                if (!crc32s.contains(crc32)) {
                    crc32s.add(crc32);
                }

                ConsumerEntity consumerEntity = new ConsumerEntity();
                consumerEntity.setConsumerNick(consumerNick);
                consumerEntity.setConsumerNickCrc32(crc32);
                consumerEntity.setSellerId(sellerId);
                consumerEntity.setFirstPaid(firstPaid);
                consumerEntity.setLatestPaid(latestPaid);
                consumerEntity.setCountOfBills(countOfBills);

                if (!newConsumersHash.contains(crc32)) {
                    newConsumersHash.put(consumerNick, consumerEntity);
                }
            }

            Pageable pageable = new PageRequest(0, 500);
            while (true) {
                List<ConsumerEntity> existedConsumers = new ArrayList<ConsumerEntity>();
                if (crc32s.size() > 0) {
                    existedConsumers.addAll(consumerEntityRepository.findAllConsumerEntitiesByCRC32(sellerId, crc32s, pageable));
                }
                for (ConsumerEntity existed : existedConsumers) {
                    String existedConsumerNick = existed.getConsumerNick();
                    if (newConsumersHash.containsKey(existedConsumerNick)) {
                        ConsumerEntity newConsumerEntity = newConsumersHash.get(existedConsumerNick);
                        existed.setCountOfBills(newConsumerEntity.getCountOfBills());
                        existed.setLatestPaid(newConsumerEntity.getLatestPaid());
                        existed.setFirstPaid(newConsumerEntity.getFirstPaid());
                        newConsumersHash.put(existedConsumerNick, existed);
                    }
                }

                if (existedConsumers.size() < 500) {
                    break;
                }
            }

            consumerEntityRepository.save(newConsumersHash.values());

            if (consumersIndex.size() < 500) {
                break;
            }
            pageRequest = pageRequest.next();
        }
    }

    @Deprecated
    @Transactional
    public void insertOrUpdateConsumerEntities(long sellerId, List<TradeEntity> tradeEntities) {
        if (tradeEntities == null || tradeEntities.isEmpty()) {
            return;
        }

        Hashtable<String, Long> nickToCountOfTradeEntitiesHash = new Hashtable<String, Long>();
        Hashtable<String, Date> nickToFirstPaidHash = new Hashtable<String, Date>();
        Hashtable<String, Date> nickToLatestPaidHash = new Hashtable<String, Date>();

        List crc32s = new ArrayList();
        for (TradeEntity tradeEntity : tradeEntities) {
            if (sellerId != tradeEntity.getSellerId()) {
                continue;
            }

            String buyerNick = tradeEntity.getBuyerNick();

            if (!crc32s.contains(tradeEntity.getBuyerNickCrc32())) {
                crc32s.add(tradeEntity.getBuyerNickCrc32());
            }

            if (!nickToCountOfTradeEntitiesHash.containsKey(buyerNick)) {
                nickToCountOfTradeEntitiesHash.put(buyerNick, 1L);
            } else {
                nickToCountOfTradeEntitiesHash.put(buyerNick, nickToCountOfTradeEntitiesHash.get(buyerNick) + 1L);
            }

            if (!nickToFirstPaidHash.containsKey(buyerNick)) {
                nickToFirstPaidHash.put(buyerNick, tradeEntity.getPayTime());
            } else {
                Date firstPaidInHash = nickToFirstPaidHash.get(buyerNick);
                if (tradeEntity.getPayTime() != null && (firstPaidInHash == null || tradeEntity.getPayTime().before(firstPaidInHash))) {
                    nickToFirstPaidHash.put(buyerNick, tradeEntity.getPayTime());
                }
            }

            if (!nickToLatestPaidHash.containsKey(buyerNick)) {
                nickToLatestPaidHash.put(buyerNick, tradeEntity.getPayTime());
            } else {
                Date latestPaidInHash = nickToLatestPaidHash.get(buyerNick);
                if (tradeEntity.getPayTime() != null && (latestPaidInHash == null || tradeEntity.getPayTime().after(latestPaidInHash))) {
                    nickToLatestPaidHash.put(buyerNick, tradeEntity.getPayTime());
                }
            }
        }

        Pageable pageable = new PageRequest(0, 1000);
        while (true) {
            List<ConsumerEntity> existedConsumerEntities = consumerEntityRepository.findAllConsumerEntitiesByCRC32(sellerId, crc32s, pageable);
            for (ConsumerEntity consumerEntity : existedConsumerEntities) {
                String existedConsumerNick = consumerEntity.getConsumerNick();
                if (nickToCountOfTradeEntitiesHash.containsKey(existedConsumerNick)) {
                    consumerEntity.setCountOfBills(consumerEntity.getCountOfBills() + nickToCountOfTradeEntitiesHash.get(existedConsumerNick));
                    nickToCountOfTradeEntitiesHash.remove(existedConsumerNick);
                }

                if (nickToFirstPaidHash.containsKey(existedConsumerNick)) {
                    Date firstPaid = nickToFirstPaidHash.get(existedConsumerNick);
                    if (firstPaid != null) {
                        if (consumerEntity.getFirstPaid() == null || consumerEntity.getFirstPaid().after(firstPaid)) {
                            consumerEntity.setFirstPaid(firstPaid);
                        }
                    }

                    nickToFirstPaidHash.remove(existedConsumerNick);
                }

                if (nickToLatestPaidHash.containsKey(existedConsumerNick)) {
                    Date latestPaid = nickToLatestPaidHash.get(existedConsumerNick);
                    if (latestPaid != null) {
                        if (consumerEntity.getLatestPaid() == null || latestPaid.after(consumerEntity.getLatestPaid())) {
                            consumerEntity.setLatestPaid(latestPaid);
                        }
                    }

                    nickToLatestPaidHash.remove(existedConsumerNick);
                }
            }

            consumerEntityRepository.save(existedConsumerEntities);
            if (existedConsumerEntities.size() < 1000) {
                break;
            }
        }

        nickToCountOfTradeEntitiesHash = new Hashtable<String, Long>();
        nickToFirstPaidHash = new Hashtable<String, Date>();
        nickToLatestPaidHash = new Hashtable<String, Date>();

        List<ConsumerEntity> newConsumerEntities = new ArrayList<ConsumerEntity>();
        Set<Map.Entry<String, Long>> entrySet = nickToCountOfTradeEntitiesHash.entrySet();
        for (Iterator<Map.Entry<String, Long>> it = entrySet.iterator(); it.hasNext(); ) {
            Map.Entry<String, Long> entry = it.next();
            String nick = entry.getKey();
            Long incrementalCountOfBills = entry.getValue();

            ConsumerEntity newConsumerEntity = new ConsumerEntity();
            newConsumerEntity.setCountOfBills(incrementalCountOfBills);
            newConsumerEntity.setLatestPaid(nickToLatestPaidHash.get(nick));
            newConsumerEntity.setFirstPaid(nickToFirstPaidHash.get(nick));
            newConsumerEntity.setSellerId(sellerId);
            newConsumerEntity.setConsumerNickCrc32(CommonUtils.getCRC32(nick));
            newConsumerEntity.setConsumerNick(nick);

            newConsumerEntities.add(newConsumerEntity);
        }

        consumerEntityRepository.save(newConsumerEntities);
    }

    public void insertOrUpdateProductPurchaseMeasurement(Hashtable<Long, ProductPurchaseMeasurement> measurements) {
        if (measurements == null) {
            return;
        }

        Pageable pageable = new PageRequest(0, 100);
        Set<Long> oids = new HashSet<Long>(measurements.keySet());
        while (true) {
            Page<ProductPurchaseMeasurement> page = productPurchaseMeasurementRepository.findAllByOids(oids, pageable);
            List<ProductPurchaseMeasurement> existedMeasurements = page.getContent();

            for (ProductPurchaseMeasurement existedMeasurement : existedMeasurements) {
                ProductPurchaseMeasurement newMeasurement = measurements.get(existedMeasurement.getOid());
                if (newMeasurement != null) {
                    existedMeasurement.setConsumerId(newMeasurement.getConsumerId());
                    existedMeasurement.setProductNumIid(newMeasurement.getProductNumIid());
                    existedMeasurement.setProductTitle(newMeasurement.getProductTitle());
                    existedMeasurement.setSellerId(newMeasurement.getSellerId());
                    existedMeasurement.setPayTime(newMeasurement.getPayTime());
                    existedMeasurement.setPayment(newMeasurement.getPayment());
                    existedMeasurement.setNumber(newMeasurement.getNumber());
                    existedMeasurement.setPrice(newMeasurement.getPrice());

                    measurements.remove(existedMeasurement.getOid());
                }
            }

            productPurchaseMeasurementRepository.save(existedMeasurements);

            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }

        productPurchaseMeasurementRepository.save(measurements.values());
    }

    public void insertOrUpdateProductEntities(Hashtable<Long, ProductEntity> productEntities) {
        if (productEntities == null) {
            return;
        }

        Pageable pageable = new PageRequest(0, 100);
        Set<Long> numberIids = new HashSet<Long>(productEntities.keySet());
        while (true) {
            Page<ProductEntity> page = productEntityRepository.findAllByNumberIids(numberIids, pageable);
            List<ProductEntity> existedProducts = page.getContent();

            for (ProductEntity existedProduct : existedProducts) {
                ProductEntity newProduct = productEntities.get(existedProduct.getNumberIid());
                if (newProduct != null) {
                    existedProduct.setPrice(newProduct.getPrice());
                    existedProduct.setOuterSkuId(newProduct.getOuterSkuId());
                    existedProduct.setPicturePath(newProduct.getPicturePath());
                    existedProduct.setSkuId(newProduct.getSkuId());
                    existedProduct.setSellerId(newProduct.getSellerId());
                    existedProduct.setSkuPropertiesName(newProduct.getSkuPropertiesName());
                    existedProduct.setTitle(newProduct.getTitle());
                    productEntities.put(existedProduct.getNumberIid(), existedProduct);
                }
            }

            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }

        productEntityRepository.save(productEntities.values());
    }

    public void buildProductPurchaseBy(Date dateStart, Date dateEnd) {
        if (dateStart == null) {
            dateStart = new Date(0);
        }

        if (dateEnd == null) {
            dateEnd = Calendar.getInstance().getTime();
        }

        Pageable pageable = new PageRequest(0, 100);
        while (true) {
            Page<Visitor> page = visitorRepository.findAll(pageable);
            for (Visitor v : page.getContent()) {
                buildProductPurchaseForSeller(v.getVisitorId(), dateStart, dateEnd);
            }
            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }
    }

    public void buildProductPurchaseForSeller(Long sellerId, Date dateStart, Date dateEnd) {
        Pageable pageable = new PageRequest(0, 1000);
        Hashtable<Long, ProductPurchaseMeasurement> measurements = new Hashtable<Long, ProductPurchaseMeasurement>();
        while (true) {
            Page<TradeEntity> page = tradeEntityRepository.findAllBySellerIdAndPayTime(sellerId, dateStart, dateEnd, pageable);
            List<TradeEntity> tradeEntities = page.getContent();


            for (TradeEntity tradeEntity : tradeEntities) {
                List<OrderEntity> orderEntities = tradeEntity.getOrderEntities();
                for (OrderEntity orderEntity : orderEntities) {
                    ConsumerEntity consumer = consumerEntityRepository.findOneByConsumerNickCrc32AndConsumerNick(
                            CommonUtils.getCRC32(tradeEntity.getBuyerNick()), tradeEntity.getBuyerNick());
                    if (consumer == null) {
                        System.out.println("Consumer " + tradeEntity.getBuyerNick() + " of seller ID " + sellerId + " doesn't exist.");
                        continue;
                    }
                    ProductPurchaseMeasurement m = new ProductPurchaseMeasurement();
                    m.setConsumerId(consumer.getId());
                    m.setNumber(orderEntity.getNumber());
                    m.setPrice(orderEntity.getPrice());
                    m.setOid(orderEntity.getOid());
                    m.setPayment(orderEntity.getPayment());
                    m.setPayTime(tradeEntity.getPayTime());
                    m.setProductNumIid(orderEntity.getNumberIid());
                    m.setProductTitle(orderEntity.getTitle());
                    m.setSellerId(sellerId);

                    measurements.put(orderEntity.getOid(), m);

                    if (measurements.size() >= 1000) {
                        insertOrUpdateProductPurchaseMeasurement(measurements);
                        measurements.clear();
                    }
                }
            }

            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }

        if (measurements.size() > 0) {
            insertOrUpdateProductPurchaseMeasurement(measurements);
            measurements.clear();
        }
    }

    public void buildProductEntitiesBy(Date dateStart, Date dateEnd) {
        if (dateStart == null) {
            dateStart = new Date(0);
        }

        if (dateEnd == null) {
            dateEnd = Calendar.getInstance().getTime();
        }

        Pageable pageable = new PageRequest(0, 100);
        while (true) {
            Page<Visitor> page = visitorRepository.findAll(pageable);
            for (Visitor v : page.getContent()) {
                buildProductEntitiesForSeller(v.getVisitorId(), dateStart, dateEnd);
            }
            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }
    }

    public void buildProductEntitiesForSeller(long sellerId, Date dateStart, Date dateEnd) {
        Pageable pageable = new PageRequest(0, 1000);
        Hashtable<Long, ProductEntity> productEntityHashtable = new Hashtable<Long, ProductEntity>();
        while (true) {
            Page<TradeEntity> page = tradeEntityRepository.findAllBySellerIdAndPayTime(sellerId, dateStart, dateEnd, pageable);
            List<TradeEntity> tradeEntities = page.getContent();
            for (TradeEntity tradeEntity : tradeEntities) {
                List<OrderEntity> orderEntities = tradeEntity.getOrderEntities();
                for (OrderEntity orderEntity : orderEntities) {

                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setPrice(orderEntity.getPrice());
                    productEntity.setNumberIid(orderEntity.getNumberIid());
                    productEntity.setSellerId(sellerId);
                    productEntity.setOuterSkuId(orderEntity.getOuterSkuId());
                    productEntity.setPicturePath(orderEntity.getPicturePath());
                    productEntity.setSkuId(orderEntity.getSkuId());
                    productEntity.setSkuPropertiesName(orderEntity.getSkuPropertiesName());
                    productEntity.setTitle(orderEntity.getTitle());

                    productEntityHashtable.put(orderEntity.getNumberIid(), productEntity);

                    insertOrUpdateProductEntitiesWithThresholdAndClear(productEntityHashtable, 1000);
                }
            }

            if (!page.hasNext()) {
                break;
            } else {
                pageable = pageable.next();
            }
        }

        insertOrUpdateProductEntitiesWithThresholdAndClear(productEntityHashtable, 1);
    }

    private void insertOrUpdateProductEntitiesWithThresholdAndClear(Hashtable<Long, ProductEntity> productEntityHashtable, long threshold) {
        if (productEntityHashtable != null && productEntityHashtable.size() >= threshold) {
            insertOrUpdateProductEntities(productEntityHashtable);
            productEntityHashtable.clear();
        }
    }

    public ConsumerEntity findConsumerEntityById(long consumerId) {
        return consumerEntityRepository.findOne(consumerId);
    }
}
