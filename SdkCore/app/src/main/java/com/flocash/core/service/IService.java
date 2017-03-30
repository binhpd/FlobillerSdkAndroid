package com.flocash.core.service;

import android.support.annotation.Keep;

import com.flocash.core.service.entity.Request;
import com.flocash.core.service.entity.Response;

import java.util.Hashtable;

/**
 * Created by ${binhpd} on 3/15/2017.
 */
@Keep
public interface IService {
    Response createOrder(Request request);

    Response updatePaymentOption(Request request);

    Response updateAdditionField(String traceNumber, Hashtable<String,String> params);

    Response getOrder(String traceNumber);
}
