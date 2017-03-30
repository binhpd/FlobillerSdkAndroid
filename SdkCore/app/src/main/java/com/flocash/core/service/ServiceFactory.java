package com.flocash.core.service;

import android.support.annotation.Keep;

/**
 * Created by ${binhpd} on 3/15/2017.
 */

@Keep
public class ServiceFactory {
    public IService getSerivce(String username, String password, String baseUrl, String orderPath) {
        return new FlocashService(username, password, baseUrl, orderPath);
    }
}
