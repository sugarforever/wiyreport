package com.wiysoft.report.mvc.controller;

import com.wiysoft.report.Constants;
import com.wiysoft.report.service.reports.ChartsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by weiliyang on 9/14/15.
 */
@Controller
@RequestMapping("/widget")
public class WidgetController {

    @Autowired
    private ChartsReportService chartsReportService;

    public static final String REQUEST_ATTR_CONSUMER_ID = "consumerId";

    @RequestMapping("/product-selector")
    public String index(@RequestParam(required = false) Long consumerId, HttpServletRequest request, HttpSession session) {
        if (session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER) == null) {
            return "redirect:/authorize";
        }

        request.setAttribute(REQUEST_ATTR_CONSUMER_ID, consumerId);
        return "/_widget/product-selector";
    }
}
