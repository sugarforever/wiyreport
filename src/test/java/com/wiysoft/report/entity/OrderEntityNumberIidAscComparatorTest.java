package com.wiysoft.report.entity;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by weiliyang on 9/18/15.
 */
public class OrderEntityNumberIidAscComparatorTest {

    @Test
    public void testCompare() {
        OrderEntity o1 = new OrderEntity();
        o1.setNumberIid(1L);
        OrderEntity o2 = new OrderEntity();
        o2.setNumberIid(2L);

        List<OrderEntity> entities = Arrays.asList(new OrderEntity[]{o1, o2});
        Collections.sort(entities, new OrderEntity.OrderEntityNumberIidAscComparator());

        Assert.assertEquals(entities.get(0), o1);
        Assert.assertEquals(entities.get(1), o2);
    }

    @Test
    public void testCompare2() {
        OrderEntity o1 = new OrderEntity();
        o1.setNumberIid(3L);
        OrderEntity o2 = new OrderEntity();
        o2.setNumberIid(2L);

        List<OrderEntity> entities = Arrays.asList(new OrderEntity[]{o1, o2});
        Collections.sort(entities, new OrderEntity.OrderEntityNumberIidAscComparator());

        Assert.assertEquals(entities.get(0), o2);
        Assert.assertEquals(entities.get(1), o1);
    }

    @Test
    public void testCompare3() {
        OrderEntity o1 = new OrderEntity();
        o1.setNumberIid(2L);
        OrderEntity o2 = new OrderEntity();
        o2.setNumberIid(2L);

        List<OrderEntity> entities = Arrays.asList(new OrderEntity[]{o1, o2});
        Collections.sort(entities, new OrderEntity.OrderEntityNumberIidAscComparator());

        Assert.assertEquals(entities.get(0), o1);
        Assert.assertEquals(entities.get(1), o2);
    }

    @Test
    public void testCompare4() {
        OrderEntity o1 = null;
        OrderEntity o2 = new OrderEntity();
        o2.setNumberIid(2L);

        List<OrderEntity> entities = Arrays.asList(new OrderEntity[]{o1, o2});
        Collections.sort(entities, new OrderEntity.OrderEntityNumberIidAscComparator());

        Assert.assertEquals(entities.get(0), o1);
        Assert.assertEquals(entities.get(1), o2);
    }

    @Test
    public void testCompare5() {
        OrderEntity o1 = new OrderEntity();
        o1.setNumberIid(1L);
        OrderEntity o2 = null;

        List<OrderEntity> entities = Arrays.asList(new OrderEntity[]{o1, o2});
        Collections.sort(entities, new OrderEntity.OrderEntityNumberIidAscComparator());

        Assert.assertEquals(entities.get(0), o2);
        Assert.assertEquals(entities.get(1), o1);
    }
}
