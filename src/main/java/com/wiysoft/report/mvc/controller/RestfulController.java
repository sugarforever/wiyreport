package com.wiysoft.report.mvc.controller;

import com.wiysoft.report.Constants;
import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.entity.ConsumerEntity;
import com.wiysoft.report.entity.ProductEntity;
import com.wiysoft.report.entity.TimeRangeEntity;
import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.mvc.model.TimeRange;
import com.wiysoft.report.repository.ConsumerEntityRepository;
import com.wiysoft.report.repository.ProductEntityRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.repository.VisitorRepository;
import com.wiysoft.report.service.CommonService;
import com.wiysoft.report.service.RefreshTokenJob;
import com.wiysoft.report.service.model.ChartsData;
import com.wiysoft.report.service.reports.Category;
import com.wiysoft.report.service.reports.ChartsReportService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by weiliyang on 7/24/15.
 */
@RestController
@RequestMapping("/rest")
public class RestfulController {

    private final static Logger logger = LoggerFactory.getLogger(RestfulController.class);

    @Autowired
    private RefreshTokenJob refreshTokenJob;
    @Autowired
    private CommonService commonService;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private ConsumerEntityRepository consumerEntityRepository;
    @Autowired
    private ProductEntityRepository productEntityRepository;
    @Autowired
    private ChartsReportService chartsReportService;

    @RequestMapping("/token/refresh/")
    public Object refreshToken() {
        for (Visitor visitor : visitorRepository.findAll()) {
            refreshTokenJob.run(visitor);
        }
        return visitorRepository.findAll();
    }

    @RequestMapping("/trade/sync/")
    public Object syncTrade() {
        for (Visitor visitor : visitorRepository.findAll()) {
            commonService.syncTrades(visitor);
        }
        return visitorRepository.findAll(new PageRequest(0, 100));
    }

    @RequestMapping("/visitors/{page}")
    public Object getVisitors(@PathVariable int page) {
        Pageable pageable = new PageRequest(page, 100);
        return visitorRepository.findAll(pageable).getContent();
    }

    @RequestMapping("/trade/created/min")
    public Object getMinCreatedByNotStatus(@RequestParam String notStatus) {
        Map<String, Date> map = new Hashtable<String, Date>();
        for (Visitor visitor : visitorRepository.findAll()) {
            Date minCreated = tradeEntityRepository.findMinCreatedBySellerIdAndNotStatus(visitor.getVisitorId(), Arrays.asList(new String[]{
                    "TRADE_FINISHED",
                    "TRADE_CLOSED",
                    "TRADE_CLOSED_BY_TAOBAO"
            }));
            map.put(visitor.getVisitorNick(), minCreated);
        }

        return map;
    }

    @RequestMapping("/trade/{page}")
    public Object getTrades(@PathVariable int page) {
        return tradeEntityRepository.findAll(new PageRequest(page, 10));
    }

    @RequestMapping(value = "/backdoor/{visitorId}")
    public Object backdoorLogin(@PathVariable long visitorId, HttpSession session) {
        Visitor visitor = visitorRepository.findOne(visitorId);
        if (visitor != null) {
            session.setAttribute(Constants.SESSION_ATTR_LOGIN_USER, visitor);
            logger.info(visitor.getVisitorNick() + " back door logged in.");
        }
        return visitor;
    }

    @RequestMapping(value = "/report/total-fee")
    public Object getReportOfSumOfTotalFee(@RequestParam(required = false) String startDateTime,
                                           @RequestParam(required = false) String endDateTime,
                                           @RequestParam(required = false) String dateTimeFormat, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }

        TimeRange timeRange = new TimeRange(startDateTime, endDateTime, dateTimeFormat);

        ChartsData chartsData = chartsReportService.reportSumTotalFeeBySellerIdStatusNotIn(
                visitor.getVisitorId(), Arrays.asList(Constants.NON_STATISTICS_TRADE_STATUS),
                timeRange.getStartDate(), timeRange.getEndDate(), "%Y-%m-%d", "yyyy/MM/dd", Calendar.DAY_OF_YEAR);

        return chartsData;
    }

    @RequestMapping(value = "/report/total-volumn")
    public Object getReportOfCountOfTrades(@RequestParam(required = false) String startDateTime,
                                           @RequestParam(required = false) String endDateTime,
                                           @RequestParam(required = false) String dateTimeFormat, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }

        TimeRange timeRange = new TimeRange(startDateTime, endDateTime, dateTimeFormat);

        ChartsData chartsData = chartsReportService.reportCountOfTradesBySellerIdStatusNotIn(
                visitor.getVisitorId(), Arrays.asList(Constants.NON_STATISTICS_TRADE_STATUS),
                timeRange.getStartDate(), timeRange.getEndDate(), "%Y-%m-%d", "yyyy/MM/dd", Calendar.DAY_OF_YEAR);

        return chartsData;
    }

    @RequestMapping(value = "/report/consumers/{page}")
    public Object getReportConsumers(@PathVariable int page, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }
        Page<ConsumerEntity> consumerEntitiesPage = consumerEntityRepository.findAllConsumerEntitiesOrderByCountOfBills(visitor.getVisitorId(), new PageRequest(page, 50));
        return consumerEntitiesPage.getContent();
    }

    @RequestMapping(value = "/report/products/{page}")
    public Object getReportProducts(@PathVariable int page, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }
        Page<ProductEntity> p = productEntityRepository.findAllBySellerId(visitor.getVisitorId(), new PageRequest(page, 50));
        return p.getContent();
    }

    @RequestMapping(value = "/report/product-purchase/timeline/{consumerId}/{numberIid}")
    public Object getReportProductPurchaseTimeline(@PathVariable long consumerId, @PathVariable long numberIid) {
        return chartsReportService.reportProductPurchaseTimeline(consumerId, numberIid);
    }

    @RequestMapping(value = "/report/product-purchase/product/{consumerId}/{page}")
    public Object getReportProductPurchaseProductsByConsumerId(@PathVariable long consumerId, @PathVariable int page) {
        return chartsReportService.reportProductPurchaseProductsByConsumerId(consumerId, page);
    }

    /*
    @RequestMapping(value = "/report/product-purchase-combo/")
    public Object getReportProductPurchaseComboBy(HttpSession session) {
        return getReportProductPurchaseComboBy(null, null, session);
    }
    */

    @RequestMapping(value = "/report/product-purchase-combo/{productNumberIid}/")
    public Object getReportProductPurchaseComboByProductNumberIid(@PathVariable long productNumberIid, HttpSession session) {
        return getReportProductPurchaseComboByProductNumberIid(productNumberIid, null, null, session);
    }

    /*
    @RequestMapping(value = "/report/product-purchase-combo/{startDate}/{endDate}/")
    public Object getReportProductPurchaseComboBy(@PathVariable String startDate, @PathVariable String endDate, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }

        Date dateStart = (startDate == null ? new Date(0) : CommonUtils.parseStrToDate(startDate, "yyyy-MM-dd"));
        Date dateEnd = (endDate == null ? DateTimeUtils.dateAdjust(Calendar.getInstance().getTime(), Calendar.DAY_OF_YEAR, 1) :
                DateTimeUtils.dateAdjust(CommonUtils.parseStrToDate(endDate, "yyyy-MM-dd"), Calendar.DAY_OF_YEAR, 1));
        return chartsReportService.reportProductPurchaseComboBySellerIdAndPayTime(visitor.getVisitorId(), dateStart, dateEnd, null);
    }
    */

    @RequestMapping(value = "/report/product-purchase-combo/{productNumberIid}/{startDate}/{endDate}/")
    public Object getReportProductPurchaseComboByProductNumberIid(@PathVariable long productNumberIid, @PathVariable String startDate, @PathVariable String endDate, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }

        Date dateStart = (startDate == null ? new Date(0) : CommonUtils.parseStrToDate(startDate, "yyyy-MM-dd"));
        Date dateEnd = (endDate == null ? DateTimeUtils.dateAdjust(Calendar.getInstance().getTime(), Calendar.DAY_OF_YEAR, 1) :
                DateTimeUtils.dateAdjust(CommonUtils.parseStrToDate(endDate, "yyyy-MM-dd"), Calendar.DAY_OF_YEAR, 1));
        return chartsReportService.reportProductPurchaseComboBySellerIdAndPayTime(visitor.getVisitorId(), dateStart, dateEnd, productNumberIid);
    }

    @RequestMapping(value = "/report/product-purchase-time-range/{category}/{productNumberIids}/{timeRangeFormat}/{timeRangeExpression}")
    public Object getReportProductPurchaseByProductNumberIidWithinTimeRanges(@PathVariable String category, @PathVariable String productNumberIids, @PathVariable String timeRangeFormat, @PathVariable String timeRangeExpression, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null || StringUtils.isEmpty(timeRangeExpression) || productNumberIids == null) {
            return null;
        }

        String[] strTimeRanges = timeRangeExpression.split("\\|");
        if (strTimeRanges.length % 2 != 0) {
            return null;
        }

        List productNumberIidList = new ArrayList();
        for (String s : productNumberIids.split("\\|")) {
            productNumberIidList.add(Long.parseLong(s));
        }

        List<TimeRangeEntity> timeRangeEntities = new ArrayList<TimeRangeEntity>();
        for (int i = 0; i < strTimeRanges.length - 1; i = i + 2) {
            String strDateStart = strTimeRanges[i];
            String strDateEnd = strTimeRanges[i + 1];

            TimeRangeEntity timeRangeEntity = new TimeRangeEntity();
            timeRangeEntity.setStartDate(CommonUtils.parseStrToDate(strDateStart, timeRangeFormat));
            timeRangeEntity.setEndDate(CommonUtils.parseStrToDate(strDateEnd, timeRangeFormat));

            timeRangeEntities.add(timeRangeEntity);
        }

        if ("payment".equals(category)) {
            return chartsReportService.reportProductPurchaseOfProductNumberIidWithinTimeRangesBySellerid(Category.payment,
                    visitor.getVisitorId(), productNumberIidList, timeRangeEntities, timeRangeFormat);
        } else if ("number".equals(category)) {
            return chartsReportService.reportProductPurchaseOfProductNumberIidWithinTimeRangesBySellerid(Category.number,
                    visitor.getVisitorId(), productNumberIidList, timeRangeEntities, timeRangeFormat);
        } else {
            return null;
        }
    }
}
