package com.flocash.core.service;

import com.flocash.core.service.entity.HttpMethod;
import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by ThaiThinh on 6/13/2015.
 */

public class FlocashService extends BaseService {
    private String baseUrl;

    public FlocashService(String userName, String password, String baseUrl, String orderPath) {
        super(userName, password);
        ConfigUrl.BASE_URL = baseUrl;
        ConfigUrl.ORDER_PATH = orderPath;
        this.baseUrl = baseUrl;
    }

    public Response createOrder(Request request) {
        String requestStr = gson.toJson(request);
        String url = baseUrl + ConfigUrl.ORDER_PATH;
        Response response = new Response();
        response.setSuccess(false);
        try {
            HttpResponse httpResponse = call(url, requestStr, HttpMethod.POST);
            String errorCode = getErrorCode(httpResponse);
            if (errorCode == null) {
                response.setSuccess(true);
                HttpEntity entity = httpResponse.getEntity();
                Response r = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Response.class);
                response.setOrder(r.getOrder());
                response.setPaymentOptions(r.getPaymentOptions());
            } else {
                response.setErrorCode(errorCode);
                response.setErrorMessage(getErrorMsg(httpResponse));
            }
        } catch (IOException e) {
            response.setSuccess(false);
        }
        return response;
    }
    public Response updatePaymentOption(Request request) {
        String requestStr = gson.toJson(request);
        String url = baseUrl + ConfigUrl.ORDER_PATH;
        Response response = new Response();
        response.setSuccess(false);
        try {
            HttpResponse httpResponse = call(url, requestStr, HttpMethod.PUT);
            String errorCode = getErrorCode(httpResponse);
            if (errorCode == null) {
                response.setSuccess(true);
                HttpEntity entity = httpResponse.getEntity();
                Response r = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Response.class);
                response.setOrder(r.getOrder());
            }else{
                response.setErrorCode(errorCode);
                response.setErrorMessage(getErrorMsg(httpResponse));
            }
        } catch (IOException e) {
            response.setSuccess(false);
        }
        return response;
    }
    public Response updateAdditionField(String traceNumber, Hashtable<String,String> params) {
        String url = baseUrl + ConfigUrl.ORDER_PATH+"/"+traceNumber;
        Response response = new Response();
        response.setSuccess(false);
        try {
            HttpResponse httpResponse = postForm(url, params);
            String errorCode = getErrorCode(httpResponse);
            if (errorCode == null) {
                response.setSuccess(true);
                HttpEntity entity = httpResponse.getEntity();
                Response r = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Response.class);
                response.setOrder(r.getOrder());
            }else{
                response.setErrorCode(errorCode);
                response.setErrorMessage(getErrorMsg(httpResponse));
            }
        } catch (IOException e) {
            response.setSuccess(false);
        }
        return response;
    }
    public Response getOrder(String traceNumber) {
        String url = baseUrl + ConfigUrl.ORDER_PATH+"/"+traceNumber;
        Response response = new Response();
        response.setSuccess(false);
        try {
            HttpResponse httpResponse = call(url, null, HttpMethod.GET);
            String errorCode = getErrorCode(httpResponse);
            if (errorCode == null) {
                response.setSuccess(true);
                HttpEntity entity = httpResponse.getEntity();
                Response r = gson.fromJson(EntityUtils.toString(entity, "UTF-8"), Response.class);
                response.setOrder(r.getOrder());
            }else{
                response.setErrorCode(errorCode);
                response.setErrorMessage(getErrorMsg(httpResponse));
            }
        } catch (IOException e) {
            response.setSuccess(false);
        }
        return response;
    }
}
