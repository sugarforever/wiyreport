package com.wiysoft.report.service;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.entity.*;
import com.wiysoft.report.measurement.ProductPurchaseComboMeasurement;
import com.wiysoft.report.measurement.ProductPurchaseComboMeasurementPK;
import com.wiysoft.report.measurement.ProductPurchaseMeasurement;
import com.wiysoft.report.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(DAOService.class);

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private ConsumerEntityRepository consumerEntityRepository;
    @Autowired
    private ProductPurchaseMeasurementRepository productPurchaseMeasurementRepository;
    @Autowired
    private ProductPurchaseComboMeasurementRepository productPurchaseComboMeasurementRepository;
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
            Page<Object[]> consumersIndex = tradeEntityRepository.findAllConsumersIndex(sellerId, pageRequest);

            Hashtable<String, ConsumerEntity> newConsumersHash = new Hashtable<String, ConsumerEntity>();
            List<Long> crc32s = new ArrayList<Long>();
            for (Object[] consumerIndex : consumersIndex) {
                String consumerNick = (String) consumerIndex[0];
                Date firstPaid = (Date) consumerIndex[1];
                Date latestPaid = (Date) consumerIndex[2];
                Long countOfBills = (Long) consumerIndex[3];
                long crc32 = CommonUtils.getCRC32(consumerNick);

                if (!crc32s.contains(crc32)) {
                    crc32s.add(crc32);
                }

                ConsumerEntity consumerEntity = new ConsumerEntity(sellerId, crc32, consumerNick, firstPaid, latestPaid, countOfBills);
                if (!newConsumersHash.contains(consumerNick)) {
                    newConsumersHash.put(consumerNick, consumerEntity);
                }
            }
            if (crc32s.size() > 0) {
                Pageable pageable = new PageRequest(0, 500);
                while (true) {
                    Page<ConsumerEntity> p = consumerEntityRepository.findAllConsumerEntitiesByCRC32(sellerId, crc32s, pageable);
                    List<ConsumerEntity> existedConsumers = p.getContent();

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

                    if (!p.hasNext()) {
                        break;
                    } else {
                        pageable = pageable.next();
                    }
                }
            }
            consumerEntityRepository.save(newConsumersHash.values());
            if (!consumersIndex.hasNext()) {
                break;
            } else {
                pageRequest = pageRequest.next();
            }
        }
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

    @Transactional
    public void buildProductPurchaseForSeller(Long sellerId, Date dateStart, Date dateEnd) {
        Pageable pageable = new PageRequest(0, 1000);
        Hashtable<Long, ProductPurchaseMeasurement> measurements = new Hashtable<Long, ProductPurchaseMeasurement>();
        while (true) {
            Page<TradeEntity> page = tradeEntityRepository.findAllBySellerIdAndPayTime(sellerId, dateStart, dateEnd, pageable);
            List<TradeEntity> tradeEntities = page.getContent();

            if (tradeEntities.size() > 0) {
                List crc32s = getCrc32sListByTradeEntities(tradeEntities);
                Hashtable<String, ConsumerEntity> consumerEntityHashtable = getConsumerEntityHashtableByConsumerNickCrc32s(sellerId, crc32s);
                for (TradeEntity tradeEntity : tradeEntities) {
                    ConsumerEntity consumerEntity = consumerEntityHashtable.get(tradeEntity.getBuyerNick());
                    if (consumerEntity == null) {
                        logger.warn("Consumer " + tradeEntity.getBuyerNick() + " of seller ID " + sellerId + " doesn't exist.");
                        continue;
                    }

                    List<OrderEntity> orderEntities = tradeEntity.getOrderEntities();
                    for (OrderEntity orderEntity : orderEntities) {
                        ProductPurchaseMeasurement m = new ProductPurchaseMeasurement(orderEntity.getOid(), orderEntity.getNumberIid(),
                                orderEntity.getTitle(), sellerId, consumerEntity.getId(), tradeEntity.getPayTime(), orderEntity.getPayment(),
                                orderEntity.getNumber(), orderEntity.getPrice());

                        measurements.put(orderEntity.getOid(), m);
                        if (measurements.size() >= 1000) {
                            insertOrUpdateProductPurchaseMeasurement(measurements);
                            measurements.clear();
                        }
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

    private List getCrc32sListByTradeEntities(List<TradeEntity> tradeEntities) {
        List crc32s = new ArrayList();
        for (TradeEntity tradeEntity : tradeEntities) {
            Long crc32 = CommonUtils.getCRC32(tradeEntity.getBuyerNick());
            if (!crc32s.contains(crc32)) {
                crc32s.add(crc32);
            }
        }
        return crc32s;
    }

    private Hashtable<String, ConsumerEntity> getConsumerEntityHashtableByConsumerNickCrc32s(Long sellerId, List crc32s) {
        Hashtable<String, ConsumerEntity> consumerEntityHashtable = new Hashtable<String, ConsumerEntity>();
        Pageable pagination = new PageRequest(0, 1000);
        while (true) {
            Page<ConsumerEntity> queryPage = consumerEntityRepository.findAllConsumerEntitiesByCRC32(sellerId, crc32s, pagination);
            for (ConsumerEntity e : queryPage.getContent()) {
                consumerEntityHashtable.put(e.getConsumerNick(), e);
            }

            if (queryPage.hasNext()) {
                pagination = pagination.next();
            } else {
                break;
            }
        }

        return consumerEntityHashtable;
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

    @Transactional
    public void buildProductEntitiesForSeller(long sellerId, Date dateStart, Date dateEnd) {
        Pageable pageable = new PageRequest(0, 1000);
        Hashtable<Long, ProductEntity> productEntityHashtable = new Hashtable<Long, ProductEntity>();
        while (true) {
            Page<TradeEntity> page = tradeEntityRepository.findAllBySellerIdAndPayTime(sellerId, dateStart, dateEnd, pageable);
            List<TradeEntity> tradeEntities = page.getContent();
            for (TradeEntity tradeEntity : tradeEntities) {
                List<OrderEntity> orderEntities = tradeEntity.getOrderEntities();
                for (OrderEntity orderEntity : orderEntities) {
                    ProductEntity productEntity = new ProductEntity(orderEntity.getNumberIid(), sellerId, orderEntity.getTitle(),
                            orderEntity.getPicturePath(), orderEntity.getPrice(), orderEntity.getSkuId(),
                            orderEntity.getOuterSkuId(), orderEntity.getSkuPropertiesName());
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

    @Transactional
    public void buildProductPurchaseComboMeasurements() {
        Date maxPayTimeInProductPurchaseComboMeasurements = productPurchaseComboMeasurementRepository.findMaxPayTime();
        Date dateStart = (maxPayTimeInProductPurchaseComboMeasurements == null ? new Date(0) : maxPayTimeInProductPurchaseComboMeasurements);
        Date dateEnd = Calendar.getInstance().getTime();

        List<ProductPurchaseComboMeasurement> measurements = new ArrayList<ProductPurchaseComboMeasurement>();

        Pageable pageRequest = new PageRequest(0, 1000);
        while (true) {
            Page<TradeEntity> page = tradeEntityRepository.findAllByPayTime(dateStart, dateEnd, pageRequest);
            for (TradeEntity tradeEntity : page.getContent()) {
                List<OrderEntity> orders = tradeEntity.getOrderEntities();
                if (orders == null || orders.size() < 2) {
                    continue;
                }

                Collections.sort(orders, new OrderEntity.OrderEntityNumberIidAscComparator());
                int len = orders.size();

                for (int i = 0; i < len - 1; ++i) {
                    for (int j = i + 1; j < len; ++j) {
                        OrderEntity o1 = orders.get(i);
                        OrderEntity o2 = orders.get(j);

                        ProductPurchaseComboMeasurement measurement = new ProductPurchaseComboMeasurement();
                        ProductPurchaseComboMeasurementPK pk = new ProductPurchaseComboMeasurementPK();
                        pk.setTid(tradeEntity.getTid());
                        pk.setProductOid(o1.getOid());
                        pk.setAnotherProductOid(o2.getOid());
                        measurement.setProductPurchaseComboMeasurementPK(pk);
                        measurement.setSellerId(tradeEntity.getSellerId());
                        measurement.setProductNumberIid(o1.getNumberIid());
                        measurement.setAnotherProductNumberIid(o2.getNumberIid());
                        measurement.setPayTime(tradeEntity.getPayTime());

                        measurements.add(measurement);
                    }
                }

                insertOrUpdateProductPurchaseComboMeasurementWithThresholdAndClear(measurements, 500);
            }

            if (page.hasNext()) {
                pageRequest = pageRequest.next();
            } else {
                break;
            }
        }

        insertOrUpdateProductPurchaseComboMeasurementWithThresholdAndClear(measurements, 1);
    }

    private void insertOrUpdateProductPurchaseComboMeasurementWithThresholdAndClear(List<ProductPurchaseComboMeasurement> measurements, int threshold) {
        if (measurements != null && measurements.size() >= threshold) {
            productPurchaseComboMeasurementRepository.save(measurements);
            measurements.clear();
        }
    }

    public Date findMaxPayTimeInProductPurchaseMeasurement() {
        return productPurchaseComboMeasurementRepository.findMaxPayTime();
    }
}
