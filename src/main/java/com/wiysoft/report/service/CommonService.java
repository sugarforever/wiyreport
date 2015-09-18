package com.wiysoft.report.service;

import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.top.link.embedded.websocket.util.StringUtil;
import com.wiysoft.report.WiyReportConfiguration;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.common.EntityBuilder;
import com.wiysoft.report.entity.TradeEntity;
import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.repository.TradeEntityRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by weiliyang on 8/24/15.
 */

@Service
public class CommonService {

    @Autowired
    private WiyReportConfiguration wiyReportConfiguration;
    @Autowired
    private TradeEntityRepository tradeEntityRepository;
    @Autowired
    private DAOService daoService;

    public String getOAuth2Url() {
        String base = wiyReportConfiguration.getBaseAuthUrl();
        String clientId = wiyReportConfiguration.getAppKey();
        String responseType = "code";
        String redirectUrl = wiyReportConfiguration.getCallbackUrl();

        return String.format("%s?client_id=%s&response_type=%s&redirect_uri=%s&state=1212&view=web", base, clientId, responseType, redirectUrl);
    }

    public String getTopAuthUrl() {
        String base = wiyReportConfiguration.getTopAuthUrl();
        String clientId = wiyReportConfiguration.getAppKey();
        return base + clientId;
    }

    public String getRefreshTokenUrl(boolean sandbox, String refreshToken, String sessionKey) {
        String refreshUrl = null;
        try {
            Map<String, String> signParams = new TreeMap<String, String>();
            signParams.put("appkey", wiyReportConfiguration.getAppKey());
            signParams.put("refresh_token", refreshToken);
            signParams.put("sessionkey", sessionKey);

            StringBuilder paramsString = new StringBuilder();
            Set<Map.Entry<String, String>> paramsEntry = signParams.entrySet();
            for (Map.Entry paramEntry : paramsEntry) {
                paramsString.append(paramEntry.getKey()).append(paramEntry.getValue());
            }
            String sign = DigestUtils.md5Hex((paramsString.toString() + wiyReportConfiguration.getAppSecret()).getBytes("utf-8")).toUpperCase();
            String signEncoder = URLEncoder.encode(sign, "utf-8");
            String appkeyEncoder = URLEncoder.encode(wiyReportConfiguration.getAppKey(), "utf-8");
            String refreshTokenEncoder = URLEncoder.encode(refreshToken, "utf-8");
            String sessionKeyEncoder = URLEncoder.encode(sessionKey, "utf-8");
            refreshUrl = wiyReportConfiguration.getRefreshTokenUrl() + "appkey=" + appkeyEncoder + "&refresh_token=" + refreshTokenEncoder + "&sessionkey=" + sessionKeyEncoder + "&sign=" + signEncoder;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ;
        }

        return refreshUrl;
    }

    public String getToken(String code, String state, boolean sandbox) {
        String url = sandbox ? wiyReportConfiguration.getSandboxTokenUrl() : wiyReportConfiguration.getProductionTokenUrl();
        Map<String, String> props = new HashMap<String, String>();
        props.put("grant_type", "authorization_code");
        props.put("code", code);
        props.put("client_id", sandbox ? wiyReportConfiguration.getSandboxAppKey() : wiyReportConfiguration.getProductionAppKey());
        props.put("client_secret", sandbox ? wiyReportConfiguration.getSandboxAppSecret() : wiyReportConfiguration.getProductionAppSecret());
        props.put("redirect_uri", sandbox ? wiyReportConfiguration.getSandboxCallbackUrl() : wiyReportConfiguration.getProductionCallbackUrl());
        props.put("view", "web");
        props.put("state", state);
        String jsonResponse = null;
        try {
            jsonResponse = WebUtils.doPost(url, props, 30000, 30000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    public Map<String, String> parseTopParameters(String strTopParams, String encode) {
        if (strTopParams == null)
            return null;
        if (encode == null)
            encode = "GBK";
        String keyValues = null;
        try {
            keyValues = new String(Base64.decodeBase64(strTopParams.getBytes(encode)), encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] kvPairs = keyValues.split("\\&");
        Map<String, String> map = new HashMap<String, String>();
        for (String kv : kvPairs) {
            String[] s = kv.split("\\=");
            if (s == null || s.length != 2)
                return null;
            map.put(s[0], s[1]);
        }
        return map;
    }

    public Date now() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(wiyReportConfiguration.getTaobaoTimeZone()));
        return c.getTime();
    }

    public void syncTrades(Visitor visitor) {
        if (visitor == null) {
            return;
        }
        long pageSize = 50;
        String sessionKey = visitor.getSessionKey();
        if (StringUtils.isEmpty(sessionKey)) {
            return;
        }

        Date createdStart = getQueryStartDate(visitor);

        System.out.println(String.format("About to synchronize trades for %s since %s",
                visitor.getVisitorNick(), createdStart.toString()));

        DefaultTaobaoClient client = new DefaultTaobaoClient(wiyReportConfiguration.getRestfulApi(),
                wiyReportConfiguration.getAppKey(),
                wiyReportConfiguration.getAppSecret(),
                Constants.FORMAT_JSON,
                30000, 300000);

        TradesSoldGetRequest req = new TradesSoldGetRequest();
        req.setUseHasNext(true);
        req.setStartCreated(createdStart);
        req.setPageSize(pageSize);
        req.setFields("seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");
        try {
            long pageNumber = 1;
            while (true) {
                req.setPageNo(pageNumber);

                TradesSoldGetResponse response = client.execute(req, sessionKey);
                List<Trade> trades = response.getTrades();

                if (trades != null) {
                    Hashtable<Long, TradeEntity> existed = getLongTradeEntityHashtable(trades);

                    for (Trade trade : trades) {
                        TradeEntity entity = EntityBuilder.buildTradeEntity(trade, visitor.getVisitorId());

                        if (existed.containsKey(trade.getTid())) {
                            TradeEntity existedTradeEntity = existed.get(trade.getTid());
                            EntityBuilder.updateTradeEntity(existedTradeEntity, entity);
                            entity = existedTradeEntity;
                        }

                        tradeEntityRepository.save(entity);
                    }
                }

                if (response.getHasNext() == null || !response.getHasNext()) {
                    break;
                }
                ++pageNumber;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } finally {
            // rebuild consumer entities
            daoService.buildConsumersBy(visitor.getVisitorId());
            System.out.println(CommonService.class.getCanonicalName() + " finished.");

            // rebuild product purchase
            Date today = Calendar.getInstance().getTime();
            Date yesterday = DateTimeUtils.dateAdjust(today, Calendar.DAY_OF_YEAR, -1);
            daoService.buildProductPurchaseForSeller(visitor.getVisitorId(), yesterday, today);
            // rebuild produce entities
            daoService.buildProductEntitiesForSeller(visitor.getVisitorId(), yesterday, today);
            // build product purchase combo measurements
            daoService.buildProductPurchaseComboMeasurements();
        }
    }

    private Hashtable<Long, TradeEntity> getLongTradeEntityHashtable(List<Trade> trades) {
        Hashtable<Long, TradeEntity> existed = new Hashtable<Long, TradeEntity>();
        List<Long> tids = new ArrayList<Long>();
        for (Trade trade : trades) {
            tids.add(trade.getTid());
        }
        Collection entities = tradeEntityRepository.findTradeEntitiesByTids(tids);
        for (Object o : entities) {
            TradeEntity e = (TradeEntity) o;
            existed.put(e.getTid(), e);
        }
        return existed;
    }

    private Date getQueryStartDate(Visitor visitor) {
        Date minCreated = tradeEntityRepository.findMinCreatedBySellerIdAndNotStatus(visitor.getVisitorId(),
                Arrays.asList(com.wiysoft.report.Constants.FINAL_TRADE_STATUS));
        if (minCreated == null) {
            minCreated = new Date(0);
        }
        Date now = now();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, -3);
        now = c.getTime();

        Date createdStart = minCreated;
        if (now.after(minCreated)) {
            createdStart = now;
        }
        return createdStart;
    }
}
