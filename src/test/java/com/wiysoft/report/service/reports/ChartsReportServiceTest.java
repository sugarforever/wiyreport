package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.entity.OrderEntity;
import com.wiysoft.report.entity.ProductEntity;
import com.wiysoft.report.repository.ProductEntityRepository;
import com.wiysoft.report.repository.ProductPurchaseComboMeasurementRepository;
import com.wiysoft.report.repository.ProductPurchaseMeasurementRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.model.ChartsData;
import com.wiysoft.report.service.model.network.Data;
import com.wiysoft.report.service.model.network.Edge;
import com.wiysoft.report.service.model.network.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

/**
 * Created by weiliyang on 9/18/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChartsReportServiceTest {

    @InjectMocks
    private ChartsReportService chartsReportService;

    @Mock
    private TradeEntityRepository tradeEntityRepository;
    @Mock
    private ProductEntityRepository productEntityRepository;
    @Mock
    private ProductPurchaseMeasurementRepository productPurchaseMeasurementRepository;
    @Mock
    private ProductPurchaseComboMeasurementRepository productPurchaseComboMeasurementRepository;
    @Mock
    private LabelService labelService;

    @Test
    public void test_reportSumTotalFeeBySellerIdStatusNotIn() {
        long sellerId = 1234L;
        List status = Arrays.asList(new String[]{"PAID", "COMPLETED"});
        String sqlDateFormat = "%Y-%m-%d";
        String simpleDateFormat = "yyyy-MM-dd";
        Date startCreated = CommonUtils.parseStrToDate("2015-01-02", simpleDateFormat);
        Date endCreated = CommonUtils.parseStrToDate("2015-01-14", simpleDateFormat);


        Date d1 = CommonUtils.parseStrToDate("2015-01-02", simpleDateFormat);
        Date d2 = CommonUtils.parseStrToDate("2015-01-07", simpleDateFormat);
        Date d3 = CommonUtils.parseStrToDate("2015-01-08", simpleDateFormat);
        Date d4 = CommonUtils.parseStrToDate("2015-01-14", simpleDateFormat);

        Double fee1 = 1.1;
        Double fee2 = 2.2;
        Double fee3 = 0.0;
        Double fee4 = 4.4;
        Mockito.when(tradeEntityRepository.findSumTotalFeeBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat)).thenReturn(Arrays.asList(new Object[]{
                new Object[]{d1, fee1}, new Object[]{d2, fee2}, new Object[]{d3, fee3}, new Object[]{d4, fee4}
        }));

        ChartsData chartsData = chartsReportService.reportSumTotalFeeBySellerIdStatusNotIn(sellerId, status, startCreated, endCreated, sqlDateFormat, simpleDateFormat, Calendar.DAY_OF_YEAR);
        List dataList = chartsData.getDatasets().get(0).getData();
        List<String> labelList = chartsData.getLabels();
        Assert.assertEquals(13, dataList.size());
        Assert.assertEquals(13, labelList.size());
        Assert.assertEquals(fee1, dataList.get(0));
        Assert.assertEquals("2015-01-02", labelList.get(0));
        Assert.assertEquals(0.0, dataList.get(1));
        Assert.assertEquals("2015-01-03", labelList.get(1));
        Assert.assertEquals(0.0, dataList.get(2));
        Assert.assertEquals("2015-01-04", labelList.get(2));
        Assert.assertEquals(0.0, dataList.get(3));
        Assert.assertEquals("2015-01-05", labelList.get(3));
        Assert.assertEquals(0.0, dataList.get(4));
        Assert.assertEquals("2015-01-06", labelList.get(4));
        Assert.assertEquals(2.2, dataList.get(5));
        Assert.assertEquals("2015-01-07", labelList.get(5));
        Assert.assertEquals(0.0, dataList.get(6));
        Assert.assertEquals("2015-01-08", labelList.get(6));
        Assert.assertEquals(0.0, dataList.get(7));
        Assert.assertEquals("2015-01-09", labelList.get(7));
        Assert.assertEquals(0.0, dataList.get(8));
        Assert.assertEquals("2015-01-10", labelList.get(8));
        Assert.assertEquals(0.0, dataList.get(9));
        Assert.assertEquals("2015-01-11", labelList.get(9));
        Assert.assertEquals(0.0, dataList.get(10));
        Assert.assertEquals("2015-01-12", labelList.get(10));
        Assert.assertEquals(0.0, dataList.get(11));
        Assert.assertEquals("2015-01-13", labelList.get(11));
        Assert.assertEquals(4.4, dataList.get(12));
        Assert.assertEquals("2015-01-14", labelList.get(12));
    }

    @Test
    public void testReportProductPurchaseProductsByConsumerId() {
        long consumerId = 1234L;
        int page = 0;

        Date date1 = Calendar.getInstance().getTime();
        Date date2 = Calendar.getInstance().getTime();
        Date date3 = Calendar.getInstance().getTime();
        Page resultPage = new PageImpl(Arrays.asList(new Object[]{
                new Object[]{1111, "title1", date1},
                new Object[]{2222, "title2", date2},
                new Object[]{3333, "title3", date3}
        }));
        Mockito.when(productPurchaseMeasurementRepository.findAllProductsByConsumerId(
                consumerId, new PageRequest(page, 100))).thenReturn(resultPage);

        List products = chartsReportService.reportProductPurchaseProductsByConsumerId(consumerId, page);
        Assert.assertEquals(3, products.size());
        Assert.assertTrue(products.get(0) instanceof Hashtable);
        Assert.assertTrue(products.get(1) instanceof Hashtable);
        Assert.assertTrue(products.get(2) instanceof Hashtable);
        Assert.assertTrue(((Hashtable) products.get(0)).get("numIid").equals(1111));
        Assert.assertTrue(((Hashtable) products.get(1)).get("numIid").equals(2222));
        Assert.assertTrue(((Hashtable) products.get(2)).get("numIid").equals(3333));
        Assert.assertTrue(((Hashtable) products.get(0)).get("title").equals("title1"));
        Assert.assertTrue(((Hashtable) products.get(1)).get("title").equals("title2"));
        Assert.assertTrue(((Hashtable) products.get(2)).get("title").equals("title3"));
        Assert.assertTrue(((Hashtable) products.get(0)).get("purchased").equals(date1));
        Assert.assertTrue(((Hashtable) products.get(1)).get("purchased").equals(date2));
        Assert.assertTrue(((Hashtable) products.get(2)).get("purchased").equals(date3));
    }

    @Test
    public void testReportProductPurchaseProductsByConsumerId_withEmptyDatabaseResult() {
        long consumerId = 1234L;
        int page = 0;

        Page resultPage = new PageImpl(Arrays.asList(new Object[0]));
        Mockito.when(productPurchaseMeasurementRepository.findAllProductsByConsumerId(
                consumerId, new PageRequest(page, 100))).thenReturn(resultPage);

        List products = chartsReportService.reportProductPurchaseProductsByConsumerId(consumerId, page);
        Assert.assertEquals(0, products.size());
    }

    @Test
    public void test_reportProductPurchaseComboBySellerIdAndPayTime() {
        long sellerId = 1111L;
        long numberIid = 5555L;
        Date startDate = new Date(1000);
        Date endDate = new Date(2000);

        Page resultPage = new PageImpl(Arrays.asList(new Object[]{
                new Object[]{5555L, 6666L, 100L},
                new Object[]{5555L, 7777L, 200L},
                new Object[]{3333L, 5555L, 300L}
        }));

        Mockito.when(productPurchaseComboMeasurementRepository.findProductPurchaseComboAndCountBySellerIdProductNumberIidAndPayTime(
                sellerId, numberIid, startDate, endDate, new PageRequest(0, 1000))).thenReturn(resultPage);

        ProductEntity p1 = new ProductEntity(3333L, sellerId, "title3333", "pic3333", 0.3f, "skuId3333", "outerSkuId3333", "skuProperty3333");
        ProductEntity p2 = new ProductEntity(5555L, sellerId, "title5555", "pic5555", 0.5f, "skuId5555", "outerSkuId5555", "skuProperty5555");
        ProductEntity p3 = new ProductEntity(6666L, sellerId, "title6666", "pic6666", 0.6f, "skuId6666", "outerSkuId6666", "skuProperty6666");
        ProductEntity p4 = new ProductEntity(7777L, sellerId, "title7777", "pic7777", 0.7f, "skuId7777", "outerSkuId7777", "skuProperty7777");
        Mockito.when(productEntityRepository.findAllByNumberIids(new HashSet<>(Arrays.asList(new Object[]{3333L, 5555L, 6666L, 7777L})), new PageRequest(0, 1000)))
                .thenReturn(new PageImpl(Arrays.asList(new Object[]{p1, p2, p3, p4})));

        Data data = chartsReportService.reportProductPurchaseComboBySellerIdAndPayTime(sellerId, startDate, endDate, numberIid);
        Assert.assertEquals(4, data.getNodes().size());
        Assert.assertEquals(3, data.getEdges().size());

        for (Edge edge : data.getEdges()) {
            if (edge.getFrom() == 5555L && edge.getTo() == 3333L) {
                Assert.assertEquals(300L, edge.getValue());
            } else if (edge.getFrom() == 5555L && edge.getTo() == 6666L) {
                Assert.assertEquals(100L, edge.getValue());
            } else if (edge.getFrom() == 5555L && edge.getTo() == 7777L) {
                Assert.assertEquals(200L, edge.getValue());
            } else {
                Assert.fail(String.format("Unexpected edge from %d to %d", edge.getFrom(), edge.getTo()));
            }
        }

        for (Node node : data.getNodes()) {
            if (node.getId() == 5555L) {
                Assert.assertEquals(3, node.getValue());
            } else if (node.getId() == 3333L) {
                Assert.assertEquals(300, node.getValue());
            } else if (node.getId() == 6666L) {
                Assert.assertEquals(100, node.getValue());
            } else if (node.getId() == 7777L) {
                Assert.assertEquals(200, node.getValue());
            } else {
                Assert.fail("Unexpected node with ID " + node.getId());
            }
        }
    }
}
