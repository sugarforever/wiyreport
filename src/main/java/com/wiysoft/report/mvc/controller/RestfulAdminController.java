package com.wiysoft.report.mvc.controller;

import com.wiysoft.report.Constants;
import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.repository.ConsumerEntityRepository;
import com.wiysoft.report.repository.TradeEntityRepository;
import com.wiysoft.report.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by weiliyang on 7/24/15.
 */
@RestController
@RequestMapping("/rest/admin")
public class RestfulAdminController {

    @Autowired
    private DAOService daoService;
    @Autowired
    private ConsumerEntityRepository consumerEntityRepository;
    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private TopService topService;

    @RequestMapping("/build-consumers/{sellerId}")
    public Object buildConsumers(@PathVariable long sellerId) {
        daoService.buildConsumersBy(sellerId);
        return consumerEntityRepository.findAllBySellerId(sellerId, new PageRequest(0, 100));
    }

    @RequestMapping("/build-buyer-nick-crc32/")
    @Transactional
    public Object buildBuyerNickCrc32() {
        tradeEntityRepository.updateTradeEntitiesBuyerNickCrc32();
        return null;
    }

    @RequestMapping("/get-user/{nick}")
    public Object getTopUserByNick(@PathVariable String nick, HttpSession session) {
        Visitor visitor = (Visitor) session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER);
        if (visitor == null) {
            return null;
        }

        return topService.getTopUserByNick(nick, visitor.getSessionKey());
    }

    @RequestMapping("/build-product-purchase/{sellerId}/{startDate}/{endDate}/")
    @Transactional
    public Object buildProductPurchase(@PathVariable long sellerId, @PathVariable String startDate, @PathVariable String endDate) {
        daoService.buildProductPurchaseForSeller(sellerId, CommonUtils.parseStrToDate(startDate, "yyyy-MM-dd"), CommonUtils.parseStrToDate(endDate, "yyyy-MM-dd"));
        return null;
    }

    @RequestMapping("/build-product-purchase/")
    @Transactional
    public Object buildAllProductPurchase() {
        daoService.buildProductPurchaseBy(new Date(0), Calendar.getInstance().getTime());
        return null;
    }

    @RequestMapping("/build-product/{sellerId}/{startDate}/{endDate}/")
    @Transactional
    public Object buildProductEntities(@PathVariable long sellerId, @PathVariable String startDate, @PathVariable String endDate) {
        daoService.buildProductEntitiesForSeller(sellerId, CommonUtils.parseStrToDate(startDate, "yyyy-MM-dd"), CommonUtils.parseStrToDate(endDate, "yyyy-MM-dd"));
        return null;
    }

    @RequestMapping("/build-product/")
    @Transactional
    public Object buildAllProductEntities() {
        daoService.buildProductEntitiesBy(new Date(0), Calendar.getInstance().getTime());
        return null;
    }
}
