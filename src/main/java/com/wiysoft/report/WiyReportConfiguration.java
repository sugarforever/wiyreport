package com.wiysoft.report;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by weiliyang on 7/24/15.
 */
@ConfigurationProperties(prefix = "wiyreport.application", locations = "classpath:application.properties")
public class WiyReportConfiguration {

    private String sandboxBaseAuthUrl;
    private String sandboxAppKey;
    private String sandboxAppSecret;
    private String sandboxCallbackUrl;
    private String sandboxTopAuthUrl;
    private String sandboxTokenUrl;
    private String sandboxTokenRefreshUrl;
    private String sandboxRestfulApi;

    private String productionBaseAuthUrl;
    private String productionAppKey;
    private String productionAppSecret;
    private String productionCallbackUrl;
    private String productionTopAuthUrl;
    private String productionTokenUrl;
    private String productionTokenRefreshUrl;
    private String productionRestfulApi;

    private boolean sandbox;
    private boolean oauth2;
    private String syncTradesJobExecutionCron;
    private String refreshTokenJobExecutionCron;
    private String taobaoTimeZone;

    private String defaultCategory;
    private String defaultReport;
    private String generalDateTimeFormat;

    public String getSandboxBaseAuthUrl() {
        return sandboxBaseAuthUrl;
    }

    public void setSandboxBaseAuthUrl(String sandboxBaseAuthUrl) {
        this.sandboxBaseAuthUrl = sandboxBaseAuthUrl;
    }

    public String getSandboxAppKey() {
        return sandboxAppKey;
    }

    public void setSandboxAppKey(String sandboxAppKey) {
        this.sandboxAppKey = sandboxAppKey;
    }

    public String getSandboxAppSecret() {
        return sandboxAppSecret;
    }

    public void setSandboxAppSecret(String sandboxAppSecret) {
        this.sandboxAppSecret = sandboxAppSecret;
    }

    public String getSandboxCallbackUrl() {
        return sandboxCallbackUrl;
    }

    public void setSandboxCallbackUrl(String sandboxCallbackUrl) {
        this.sandboxCallbackUrl = sandboxCallbackUrl;
    }

    public String getProductionBaseAuthUrl() {
        return productionBaseAuthUrl;
    }

    public void setProductionBaseAuthUrl(String productionBaseAuthUrl) {
        this.productionBaseAuthUrl = productionBaseAuthUrl;
    }

    public String getProductionAppKey() {
        return productionAppKey;
    }

    public void setProductionAppKey(String productionAppKey) {
        this.productionAppKey = productionAppKey;
    }

    public String getProductionAppSecret() {
        return productionAppSecret;
    }

    public void setProductionAppSecret(String productionAppSecret) {
        this.productionAppSecret = productionAppSecret;
    }

    public String getProductionCallbackUrl() {
        return productionCallbackUrl;
    }

    public void setProductionCallbackUrl(String productionCallbackUrl) {
        this.productionCallbackUrl = productionCallbackUrl;
    }

    public String getSandboxTopAuthUrl() {
        return sandboxTopAuthUrl;
    }

    public void setSandboxTopAuthUrl(String sandboxTopAuthUrl) {
        this.sandboxTopAuthUrl = sandboxTopAuthUrl;
    }

    public String getProductionTopAuthUrl() {
        return productionTopAuthUrl;
    }

    public void setProductionTopAuthUrl(String productionTopAuthUrl) {
        this.productionTopAuthUrl = productionTopAuthUrl;
    }

    public String getSandboxTokenUrl() {
        return sandboxTokenUrl;
    }

    public void setSandboxTokenUrl(String sandboxTokenUrl) {
        this.sandboxTokenUrl = sandboxTokenUrl;
    }

    public String getProductionTokenUrl() {
        return productionTokenUrl;
    }

    public void setProductionTokenUrl(String productionTokenUrl) {
        this.productionTokenUrl = productionTokenUrl;
    }

    public String getSandboxTokenRefreshUrl() {
        return sandboxTokenRefreshUrl;
    }

    public void setSandboxTokenRefreshUrl(String sandboxTokenRefreshUrl) {
        this.sandboxTokenRefreshUrl = sandboxTokenRefreshUrl;
    }

    public String getProductionTokenRefreshUrl() {
        return productionTokenRefreshUrl;
    }

    public void setProductionTokenRefreshUrl(String productionTokenRefreshUrl) {
        this.productionTokenRefreshUrl = productionTokenRefreshUrl;
    }

    public String getAppKey() {
        return isSandbox() ? this.sandboxAppKey : this.productionAppKey;
    }

    public String getAppSecret() {
        return isSandbox() ? this.sandboxAppSecret : this.productionAppSecret;
    }

    public String getRefreshTokenUrl() {
        return isSandbox() ? this.sandboxTokenRefreshUrl : this.productionTokenRefreshUrl;
    }

    public String getSandboxRestfulApi() {
        return sandboxRestfulApi;
    }

    public void setSandboxRestfulApi(String sandboxRestfulApi) {
        this.sandboxRestfulApi = sandboxRestfulApi;
    }

    public String getProductionRestfulApi() {
        return productionRestfulApi;
    }

    public void setProductionRestfulApi(String productionRestfulApi) {
        this.productionRestfulApi = productionRestfulApi;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getRestfulApi() {
        return isSandbox() ? getSandboxRestfulApi() : getProductionRestfulApi();
    }

    public String getTaobaoTimeZone() {
        return taobaoTimeZone;
    }

    public void setTaobaoTimeZone(String taobaoTimeZone) {
        this.taobaoTimeZone = taobaoTimeZone;
    }

    public String getSyncTradesJobExecutionCron() {
        return syncTradesJobExecutionCron;
    }

    public void setSyncTradesJobExecutionCron(String syncTradesJobExecutionCron) {
        this.syncTradesJobExecutionCron = syncTradesJobExecutionCron;
    }

    public String getTopAuthUrl() {
        return sandbox ? sandboxTopAuthUrl : productionTopAuthUrl;
    }

    public String getBaseAuthUrl() {
        return sandbox ? sandboxBaseAuthUrl : productionBaseAuthUrl;
    }

    public boolean isOauth2() {
        return oauth2;
    }

    public void setOauth2(boolean oauth2) {
        this.oauth2 = oauth2;
    }

    public String getCallbackUrl() {
        return sandbox ? sandboxCallbackUrl : productionCallbackUrl;
    }

    public String getRefreshTokenJobExecutionCron() {
        return refreshTokenJobExecutionCron;
    }

    public void setRefreshTokenJobExecutionCron(String refreshTokenJobExecutionCron) {
        this.refreshTokenJobExecutionCron = refreshTokenJobExecutionCron;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public String getDefaultReport() {
        return defaultReport;
    }

    public void setDefaultReport(String defaultReport) {
        this.defaultReport = defaultReport;
    }

    public String getGeneralDateTimeFormat() {
        return generalDateTimeFormat;
    }

    public void setGeneralDateTimeFormat(String generalDateTimeFormat) {
        this.generalDateTimeFormat = generalDateTimeFormat;
    }
}
