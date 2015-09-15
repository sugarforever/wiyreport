package com.wiysoft.report.mvc.controller;

import com.wiysoft.report.Constants;
import com.wiysoft.report.WiyReportConfiguration;
import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.repository.VisitorRepository;
import com.wiysoft.report.service.CommonService;
import com.wiysoft.report.service.DAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by weiliyang on 7/24/15.
 */
@Controller
@RequestMapping("/")
public class HttpController {

    @Autowired
    private WiyReportConfiguration wiyReportConfiguration;
    @Autowired
    private CommonService commonService;
    @Autowired
    private DAOService daoService;
    @Autowired
    private VisitorRepository visitorRepository;

    private static final String REQUEST_ATTR_CATEGORY = "category";
    private static final String REQUEST_ATTR_REPORT = "report";
    private static final String REQUEST_ATTR_CONSUMER = "consumer";
    @RequestMapping("/")
    public String index(@RequestParam(required = false) String category, @RequestParam(required = false) String report, HttpServletRequest request, HttpSession session) {
        if (session.getAttribute(Constants.SESSION_ATTR_LOGIN_USER) == null) {
            return "redirect:authorize";
        }

        request.setAttribute(REQUEST_ATTR_CATEGORY, category == null ? wiyReportConfiguration.getDefaultCategory() : category);
        request.setAttribute(REQUEST_ATTR_REPORT, report == null ? wiyReportConfiguration.getDefaultReport() : report);

        String consumerId = request.getParameter("consumerId");
        if (!StringUtils.isEmpty(consumerId))
            request.setAttribute(REQUEST_ATTR_CONSUMER, daoService.findConsumerEntityById(Long.parseLong(consumerId)));
        return "index";
    }

    @RequestMapping("/index.html")
    public String indexHtml(@RequestParam(required = false) String category, @RequestParam(required = false) String report, HttpServletRequest request, HttpSession session) {
        return index(category, report, request, session);
    }

    @RequestMapping(value = "/authorize")
    public String authorize() throws Exception {
        return "redirect:" + (wiyReportConfiguration.isOauth2() ?
                commonService.getOAuth2Url() : commonService.getTopAuthUrl());
    }

    @RequestMapping(value = "/oauth2/tooboo.do")
    public Object authorizeCallback(HttpServletRequest request) {
        String code = getParameterValue(request, "code");
        String state = getParameterValue(request, "state");
        String error = getParameterValue(request, "error");
        String errorDesc = getParameterValue(request, "error_description");

        String json = null;
        if (StringUtils.isEmpty(error)) {
            json = commonService.getToken(code, state, true);
        } else {

        }

        return json;
    }

    @RequestMapping(value = "/top/tooboo.do")
    public Object topAuthCallback(HttpServletRequest request, HttpSession session) {
        String topParams = getParameterValue(request, "top_parameters");
        String sessionKey = getParameterValue(request, "top_session");
        Map<String, String> parsed = commonService.parseTopParameters(topParams, "GBK");
        if (parsed != null) {
            parsed.put("sessionKey", sessionKey);
        }
        Visitor visitor = Visitor.build(parsed);
        Visitor updatedVisitor = daoService.insertOrUpdateVisitor(visitor);

        session.setAttribute(Constants.SESSION_ATTR_LOGIN_USER, updatedVisitor);
        return "redirect:/";
    }

    private String getParameterValue(HttpServletRequest request, String key) {
        String[] values = request.getParameterMap().get(key);
        return (values == null || values.length < 1) ? null : values[0];
    }
}
