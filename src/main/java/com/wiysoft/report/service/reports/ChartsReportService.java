package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.common.MathUtils;
import com.wiysoft.report.entity.ProductEntity;
import com.wiysoft.report.entity.TimeRangeEntity;
import com.wiysoft.report.measurement.ProductPurchaseMeasurement;
import com.wiysoft.report.repository.ProductEntityRepository;
import com.wiysoft.report.repository.ProductPurchaseComboMeasurementRepository;
import com.wiysoft.report.repository.ProductPurchaseMeasurementRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.model.ChartsData;
import com.wiysoft.report.service.model.ChartsDataset;
import com.wiysoft.report.service.model.VisData;
import com.wiysoft.report.service.model.network.Data;
import com.wiysoft.report.service.model.network.Edge;
import com.wiysoft.report.service.model.network.GroupNode;
import com.wiysoft.report.service.model.network.Node;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by weiliyang on 8/26/15.
 */
@Service
public class ChartsReportService {

    private final static Logger logger = LoggerFactory.getLogger(ChartsReportService.class);

    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private ProductPurchaseMeasurementRepository productPurchaseMeasurementRepository;
    @Autowired
    private ProductPurchaseComboMeasurementRepository productPurchaseComboMeasurementRepository;
    @Autowired
    private LabelService labelService;

    public ChartsData reportSumTotalFeeBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String sqlDateFormat, String simpleDateFormat, int step) {
        Collection collection = tradeEntityRepository.findSumTotalFeeBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat);
        return getChartsData(simpleDateFormat, collection, startCreated, endCreated, step);
    }

    public ChartsData reportCountOfTradesBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String sqlDateFormat, String simpleDateFormat, int step) {
        Collection collection = tradeEntityRepository.findCountOfTradesBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat);
        return getChartsData(simpleDateFormat, collection, startCreated, endCreated, step);
    }


    public List<VisData> reportProductPurchaseTimeline(long consumerId, long numberIid) {
        logger.debug("Generate report of product purchase timeline.");
        Pageable pageable = new PageRequest(0, 1000, new Sort(Sort.Direction.ASC, "payTime"));

        List<VisData> visData = new ArrayList<VisData>();
        int index = 1;
        while (true) {
            Page<ProductPurchaseMeasurement> page = productPurchaseMeasurementRepository.findAllByConsumerIdAndProductNumIid(
                    consumerId, numberIid, pageable
            );

            for (ProductPurchaseMeasurement measurement : page.getContent()) {
                VisData data = new VisData(index++, CommonUtils.parseStrFromDate(measurement.getPayTime(), "yyyy-MM-dd HH:mm:ss") + "\n" + measurement.getPayment().toString() + "元",
                        CommonUtils.parseStrFromDate(measurement.getPayTime(), "yyyy-MM-dd HH:mm:ss"), null, null);
                visData.add(data);
            }

            if (page.hasNext()) {
                pageable = pageable.next();
            } else {
                break;
            }
        }
        return visData;
    }

    public List reportProductPurchaseProductsByConsumerId(long consumerId, int page) {
        Page queryResult = productPurchaseMeasurementRepository.findAllProductsByConsumerId(consumerId, new PageRequest(page, 100));
        Collection collection = queryResult.getContent();
        List products = new ArrayList();
        for (Object obj : collection) {
            Hashtable hash = new Hashtable();
            Object[] objs = (Object[]) obj;
            hash.put("numIid", objs[0]);
            hash.put("title", objs[1]);
            hash.put("purchased", objs[2]);

            products.add(hash);
        }

        return products;
    }

    public Data reportProductPurchaseComboBySellerIdAndPayTime(long sellerId, Date startPayTime, Date endPayTime, long numberIid) {
        Date now = Calendar.getInstance().getTime();

        Pageable pageRequest = new PageRequest(0, 1000);
        Hashtable<Long, Node> hashGroupedNodes = new Hashtable<Long, Node>();
        Hashtable<String, Edge> hashEdges = new Hashtable<String, Edge>();
        Hashtable<Long, Node> cachedNodes = new Hashtable<Long, Node>();
        while (true) {
            Date startDate = (startPayTime == null ? new Date(0) : startPayTime);
            Date endDate = (endPayTime == null ? now : endPayTime);
            Page<Object[]> page = productPurchaseComboMeasurementRepository.findProductPurchaseComboAndCountBySellerIdProductNumberIidAndPayTime(sellerId, numberIid, startDate, endDate, pageRequest);

            for (Object[] objs : page.getContent()) {
                Long productNumberIid = (Long) objs[0];
                Long anotherProductNumberIid = (Long) objs[1];
                Long countOfTradeBills = (Long) objs[2];

                Node selfNode = (productNumberIid.equals(numberIid) ? new Node(productNumberIid, 1, null, "group1") : new Node(anotherProductNumberIid, 1, null, "group1"));
                Node connectedNode = (productNumberIid.equals(numberIid) ? new Node(anotherProductNumberIid, 1, null, "group1") : new Node(productNumberIid, 1, null, "group1"));

                if (!cachedNodes.containsKey(selfNode.getId())) {
                    cachedNodes.put(selfNode.getId(), selfNode);
                }
                cachedNodes.put(connectedNode.getId(), connectedNode);

                // Handle self node.
                if (!hashGroupedNodes.containsKey(selfNode.getId())) {
                    hashGroupedNodes.put(selfNode.getId(), selfNode);
                } else {
                    hashGroupedNodes.get(selfNode.getId()).incrementValue(1);
                }

                // Handle connected node.
                GroupNode createdGroupNode = null;
                for (Node createdNode : hashGroupedNodes.values()) {
                    if (createdNode instanceof GroupNode) {
                        GroupNode n = (GroupNode) createdNode;
                        if (n.getValue() == countOfTradeBills.longValue()) {
                            createdGroupNode = n;
                            break;
                        }
                    }
                }

                if (createdGroupNode == null) {
                    createdGroupNode = new GroupNode(connectedNode.getId(), countOfTradeBills, 1 + "件商品", null);
                    hashGroupedNodes.put(createdGroupNode.getId(), createdGroupNode);
                }
                createdGroupNode.appendUserObject(connectedNode);
                createdGroupNode.setLabel(createdGroupNode.getUserObjects().size() + "件商品");

                String edgeKey = String.valueOf(selfNode.getId()) + String.valueOf(createdGroupNode.getId());
                if (!hashEdges.containsKey(edgeKey)) {
                    hashEdges.put(edgeKey, new Edge(selfNode.getId(), createdGroupNode.getId(), countOfTradeBills, String.valueOf(countOfTradeBills)));
                }
                for (Object obj : createdGroupNode.getUserObjects()) {
                    ((Node) obj).setValue(countOfTradeBills);
                }
            }

            if (hashGroupedNodes.size() > 0) {
                Pageable numberIidPageRequest = new PageRequest(0, 1000);
                while (true) {
                    Page<ProductEntity> productEntities = productEntityRepository.findAllByNumberIids(cachedNodes.keySet(), numberIidPageRequest);
                    for (ProductEntity e : productEntities.getContent()) {
                        /*if (hashGroupedNodes.containsKey(e.getNumberIid())) {
                            Node node = hashGroupedNodes.get(e.getNumberIid());
                            if (!(node instanceof GroupNode)) {
                                node.setLabel(e.getTitle());
                            } else {

                            }
                        }*/
                        Long iid = e.getNumberIid();
                        if (cachedNodes.containsKey(iid)) {
                            Node cachedNode = cachedNodes.get(iid);
                            cachedNode.setLabel(e.getTitle());
                            cachedNode.setPicture(e.getPicturePath());
                        }
                    }
                    if (productEntities.hasNext()) {
                        numberIidPageRequest = numberIidPageRequest.next();
                    } else {
                        break;
                    }
                }
            }
            if (!page.hasNext()) {
                break;
            } else {
                pageRequest = pageRequest.next();
            }
        }

        return new Data(new ArrayList<Node>(hashGroupedNodes.values()), new ArrayList<Edge>(hashEdges.values()));
    }

    public ChartsData reportProductPurchaseOfProductNumberIidWithinTimeRangesBySellerid(Category category, long sellerId, Collection<Long> productNumberIids, List<TimeRangeEntity> timeRangeEntities, String dateFormat) {
        ChartsData chartsData = new ChartsData();

        if (timeRangeEntities == null || timeRangeEntities.size() == 0 || productNumberIids == null || productNumberIids.size() == 0) {
            return chartsData;
        }

        Hashtable<Long, ChartsDataset> chartsDatasetHashtable = new Hashtable<Long, ChartsDataset>();
        for (Long iid : productNumberIids) {
            chartsDatasetHashtable.put(iid, chartsData.appendDataset(new ChartsDataset()));
        }

        fillChartsDatasetsWithProductTitle(chartsDatasetHashtable);

        for (TimeRangeEntity timeRangeEntity : timeRangeEntities) {
            String label = labelService.getLabel(timeRangeEntity, dateFormat);
            chartsData.appendLabel(label);

            Pageable pageRequest = new PageRequest(0, 1000);
            List<Long> numIidsToBeUpdated = new ArrayList<Long>(productNumberIids);
            while (true) {
                Page resultsPage = productPurchaseMeasurementRepository.findSumPaymentAndCountBySellerIdAndProductNumIidWithinPayTime(sellerId, productNumberIids, timeRangeEntity.getStartDate(), timeRangeEntity.getEndDate(), pageRequest);
                if (resultsPage.getContent().size() > 0) {
                    List lst = resultsPage.getContent();
                    for (Object o : lst) {
                        Object[] oo = (Object[]) o;
                        Long productNumIid = (Long) oo[0];
                        Double totalPayment = (Double) oo[1];
                        Long totalNumber = (Long) oo[2];

                        if (category != null) {
                            if (category == Category.payment) {
                                chartsDatasetHashtable.get(productNumIid).appendData(totalPayment);
                                numIidsToBeUpdated.remove(productNumIid);
                            } else if (category == Category.number) {
                                chartsDatasetHashtable.get(productNumIid).appendData(totalNumber);
                                numIidsToBeUpdated.remove(productNumIid);
                            }
                        }
                    }
                }

                if (resultsPage.hasNext()) {
                    pageRequest = pageRequest.next();
                } else {
                    break;
                }
            }

            numIidsToBeUpdated.stream().forEach((numIid) -> {
                if (category != null && category == Category.payment) {
                    chartsDatasetHashtable.get(numIid).appendData(0.0);
                } else if (category != null && category == Category.number) {
                    chartsDatasetHashtable.get(numIid).appendData(0);
                }
            });
        }

        return chartsData;
    }

    private void fillChartsDatasetsWithProductTitle(final Hashtable<Long, ChartsDataset> chartsDatasetHashtable) {
        Pageable pageable = new PageRequest(0, 10000);
        while (true) {
            Page<ProductEntity> entities = productEntityRepository.findAllByNumberIids(chartsDatasetHashtable.keySet(), pageable);
            for (ProductEntity e : entities.getContent()) {
                if (chartsDatasetHashtable.containsKey(e.getNumberIid())) {
                    chartsDatasetHashtable.get(e.getNumberIid()).setLabel(e.getTitle());
                    chartsDatasetHashtable.get(e.getNumberIid()).setPicture(e.getPicturePath());
                }
            }
            if (entities.hasNext()) {
                pageable = pageable.next();
            } else {
                break;
            }
        }
    }

    private ChartsData getChartsData(String simpleDateFormat, Collection collection, Date startCreated, Date endCreated, int step) {
        ChartsData chartsData = new ChartsData();
        ChartsDataset chartsDataset = new ChartsDataset();
        SimpleDateFormat fmt = new SimpleDateFormat(simpleDateFormat);
        if (collection != null) {

            Date dateIndex = CommonUtils.parseStrToDate(CommonUtils.parseStrFromDate(startCreated, simpleDateFormat), simpleDateFormat);
            Date formattedEndCreated = CommonUtils.parseStrToDate(CommonUtils.parseStrFromDate(endCreated, simpleDateFormat), simpleDateFormat);
            for (Object o : collection) {
                Object[] oo = (Object[]) o;
                Date date = (Date) oo[0];
                date = CommonUtils.parseStrToDate(CommonUtils.parseStrFromDate(date, simpleDateFormat), simpleDateFormat);
                for (; dateIndex.before(date); ) {
                    String strDate = fmt.format(dateIndex);
                    chartsData.appendLabel(strDate);
                    chartsDataset.appendData(0.0);
                    dateIndex = DateTimeUtils.dateAdjust(dateIndex, step, 1);
                }

                String strDate = fmt.format(date);
                chartsData.appendLabel(strDate);

                Object data = oo[1];
                if (data instanceof Double) {
                    data = MathUtils.roundDouble((Double) data, 2);
                }
                chartsDataset.appendData(data);
                dateIndex = DateTimeUtils.dateAdjust(dateIndex, step, 1);
            }

            for (; !dateIndex.after(formattedEndCreated); ) {
                String strDate = fmt.format(dateIndex);
                chartsData.appendLabel(strDate);
                chartsDataset.appendData(0.0);
                dateIndex = DateTimeUtils.dateAdjust(dateIndex, step, 1);
            }
            chartsData.appendDataset(chartsDataset);
        }

        return chartsData;
    }
}
