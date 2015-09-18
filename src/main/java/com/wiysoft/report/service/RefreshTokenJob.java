package com.wiysoft.report.service;

import com.taobao.api.internal.util.WebUtils;
import com.wiysoft.report.entity.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by weiliyang on 8/24/15.
 */
@Component
public class RefreshTokenJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(RefreshTokenJob.class);

    @Autowired
    private CommonService commonService;
    @Autowired
    private DAOService daoService;

    @Override
    public void run(Object param) {
        if (param == null || !(param instanceof Visitor)) {
            return;
        }

        Visitor visitor = (Visitor) param;
        String refreshTokenUrl = commonService.getRefreshTokenUrl(true, visitor.getRefreshToken(), visitor.getSessionKey());

        String response = null;
        try {
            response = WebUtils.doPost(refreshTokenUrl, null, 30 * 1000 * 60, 30 * 1000 * 60);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        if (response != null) {
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map map = parser.parseMap(response);

            if (map.get("top_session") != null && map.get("refresh_token") != null) {
                try {
                    visitor.setW2ExpiresIn(Integer.parseInt((String) map.get("w2_expires_in")));
                    visitor.setW1ExpiresIn(Integer.parseInt((String) map.get("w1_expires_in")));
                    visitor.setReExpiresIn(Integer.parseInt((String) map.get("re_expires_in")));
                    visitor.setSessionKey((String) map.get("top_session"));
                    visitor.setExpiresIn(Integer.parseInt((String) map.get("expires_in")));
                    visitor.setR2ExpiresIn(Integer.parseInt((String) map.get("r2_expires_in")));
                    visitor.setRefreshToken((String) map.get("refresh_token"));
                    visitor.setR1ExpiresIn(Integer.parseInt((String) map.get("r1_expires_in")));

                    daoService.insertOrUpdateVisitor(visitor);
                } catch (NumberFormatException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
