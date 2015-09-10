package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.common.MathUtils;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.model.ChartsData;
import com.wiysoft.report.service.model.ChartsDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by weiliyang on 8/26/15.
 */
@Service
public class ChartsReportService {

    @Autowired
    private TradeEntityRepository tradeEntityRepository;

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
}
