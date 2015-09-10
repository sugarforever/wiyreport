package com.wiysoft.report.service;

import com.taobao.api.ApiException;
import com.taobao.api.Constants;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.domain.User;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.request.UserGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.taobao.api.response.UserGetResponse;
import com.wiysoft.report.WiyReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by weiliyang on 9/7/15.
 */
@Service
public class TopService {

    @Autowired
    private WiyReportConfiguration wiyReportConfiguration;

    public User getTopUserByNick(String nick, String sessionKey) {
        DefaultTaobaoClient client = new DefaultTaobaoClient(wiyReportConfiguration.getRestfulApi(),
                wiyReportConfiguration.getAppKey(),
                wiyReportConfiguration.getAppSecret(),
                Constants.FORMAT_JSON,
                30000, 300000);

        UserGetRequest request = new UserGetRequest();
        request.setFields("user_id,nick");

        User user = null;
        try {
            UserGetResponse response = client.execute(request, sessionKey);
            user = response.getUser();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return user;
    }
}
