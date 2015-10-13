package com.wiysoft.report.service.reports;

import com.wiysoft.report.entity.OrderEntity;
import com.wiysoft.report.entity.ProductEntity;
import com.wiysoft.report.repository.ProductEntityRepository;
import com.wiysoft.report.repository.ProductPurchaseComboMeasurementRepository;
import com.wiysoft.report.repository.ProductPurchaseMeasurementRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.model.network.Data;
import com.wiysoft.report.service.model.network.Edge;
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
        long numberIid = 1234L;
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

        HashMap edgeValues = new HashMap();
        edgeValues.put(100L,100L);
        edgeValues.put(200L,200L);
        edgeValues.put(300L,300L);
        for (Edge edge : data.getEdges()) {
            Assert.assertTrue(edgeValues.containsKey(edge.getValue()));
            edgeValues.remove(edge.getValue());
        }
        Assert.assertEquals(0, edgeValues.size());
    }
}
