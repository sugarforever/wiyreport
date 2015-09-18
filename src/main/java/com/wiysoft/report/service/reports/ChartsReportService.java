package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.common.MathUtils;
import com.wiysoft.report.entity.ProductEntity;
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
import com.wiysoft.report.service.model.network.Node;
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

    public ChartsData reportSumTotalFeeBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String sqlDateFormat, String simpleDateFormat, int step) {
        Collection collection = tradeEntityRepository.findSumTotalFeeBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat);
        return getChartsData(simpleDateFormat, collection, startCreated, endCreated, step);
    }

    public ChartsData reportCountOfTradesBySellerIdStatusNotIn(long sellerId, List<String> status, Date startCreated, Date endCreated, String sqlDateFormat, String simpleDateFormat, int step) {
        Collection collection = tradeEntityRepository.findCountOfTradesBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat);
        return getChartsData(simpleDateFormat, collection, startCreated, endCreated, step);
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

    public Data reportProductPurchaseComboBySellerIdAndPayTime(long sellerId, Date startPayTime, Date endPayTime) {
        Date now = Calendar.getInstance().getTime();

        Pageable pageRequest = new PageRequest(0, 1000);
        Hashtable<Long, Node> hashNodes = new Hashtable<Long, Node>();
        Hashtable<String, Edge> hashEdges = new Hashtable<String, Edge>();
        while (true) {
            Page<Object[]> page = productPurchaseComboMeasurementRepository.findProductPurchaseComboAndCountBySellerIdAndPayTime(sellerId,
                    (startPayTime == null ? new Date(0) : startPayTime), (endPayTime == null ? now : endPayTime), pageRequest);

            for (Object[] objs : page.getContent()) {
                Long productNumberIid = (Long) objs[0];
                Long anotherProductNumberIid = (Long) objs[1];
                Long countOfTradeBills = (Long) objs[2];

                if (!hashNodes.containsKey(productNumberIid)) {
                    hashNodes.put(productNumberIid, new Node(productNumberIid, 1, null));
                } else {
                    hashNodes.get(productNumberIid).incrementValue(1);
                }

                if (!hashNodes.containsKey(anotherProductNumberIid)) {
                    hashNodes.put(anotherProductNumberIid, new Node(anotherProductNumberIid, 1, null));
                } else {
                    hashNodes.get(anotherProductNumberIid).incrementValue(1);
                }

                String edgeKey = String.valueOf(productNumberIid) + String.valueOf(anotherProductNumberIid);
                if (hashEdges.containsKey(edgeKey)) {
                    hashEdges.get(edgeKey).incrementValue(countOfTradeBills);
                } else {
                    hashEdges.put(edgeKey, new Edge(productNumberIid, anotherProductNumberIid, countOfTradeBills, String.valueOf(countOfTradeBills)));
                }
            }

            Pageable numberIidPageRequest = new PageRequest(0, 1000);
            while (true) {
                Page<ProductEntity> productEntities = productEntityRepository.findAllByNumberIids(hashNodes.keySet(), numberIidPageRequest);
                for (ProductEntity e : productEntities.getContent()) {
                    if (hashNodes.containsKey(e.getNumberIid())) {
                        hashNodes.get(e.getNumberIid()).setLabel(e.getTitle());
                    }
                }
                if (productEntities.hasNext()) {
                    numberIidPageRequest = numberIidPageRequest.next();
                } else {
                    break;
                }
            }
            if (!page.hasNext()) {
                break;
            } else {
                pageRequest = pageRequest.next();
            }
        }

        return new Data(new ArrayList<Node>(hashNodes.values()), new ArrayList<Edge>(hashEdges.values()));
    }
}
