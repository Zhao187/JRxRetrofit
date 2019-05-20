package com.baozi.linfeng.location.rxandroid;

import android.text.TextUtils;

import com.google.gson.JsonObject;


public class RxParseInfo {
    public static final RxParseInfo DEFAULT =
            new RxParseInfo("code", "data", "msg", "200");

    private final String codeKey;
    private final String dataKey;
    private final String msgKey;
    private final String successCode;
    private CheckSuccess checkSuccess;

    public RxParseInfo(String codeKey, String dataKey, String msgKey, String successCode) {
        this.codeKey = codeKey;
        this.dataKey = dataKey;
        this.msgKey = msgKey;
        this.successCode = successCode;
    }

    public boolean hasKey(JsonObject asJsonObject) throws Exception {
        boolean hasCode = false, hasData = false, hasMsg = false;
        if (codeKey != null) {
            hasCode = asJsonObject.has(codeKey);
        }
        if (dataKey != null) {
            hasData = asJsonObject.has(dataKey);
        }
        if (msgKey != null) {
            hasMsg = asJsonObject.has(msgKey);
        }
        return hasCode && hasData && hasMsg;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public String getDataKey() {
        return dataKey;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public CheckSuccess getCheckSuccess() {
        return checkSuccess;
    }

    public RxParseInfo setCheckSuccess(CheckSuccess checkSuccess) {
        this.checkSuccess = checkSuccess;
        return this;
    }

    public boolean isSuccess(JsonObject jsonObject) {
        if (checkSuccess != null) {
            return checkSuccess.isSuccess(jsonObject);
        }
        String code = jsonObject.get(codeKey).toString();
        return TextUtils.equals(code, successCode);
    }

    public interface CheckSuccess {
        boolean isSuccess(JsonObject jsonObject);
    }
}
