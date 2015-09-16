package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.common.MathUtils;
import com.wiysoft.report.measurement.ProductPurchaseMeasurement;
import com.wiysoft.report.repository.ProductPurchaseMeasurementRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.model.ChartsData;
import com.wiysoft.report.service.model.ChartsDataset;
import com.wiysoft.report.service.model.VisData;
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
    private ProductPurchaseMeasurementRepository productPurchaseMeasurementRepository;

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
}
