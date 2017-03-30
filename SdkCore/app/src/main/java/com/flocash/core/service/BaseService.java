package com.flocash.core.service;

import android.util.Base64;

import com.flocash.core.service.entity.HttpMethod;
import com.flocash.core.utils.DateHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author HieuLT
 */
public abstract class BaseService implements IService {

    private static final int TIMEOUT_MILISEC = 70000;
    private static final String APPLICATION_JSON = "application/json";
    private static String basicAuth;
    protected Gson gson;

    public BaseService(String useName, String password) {
        basicAuth = buildBasicAuth(useName, password);
        gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                String unixTimestamp = json.getAsJsonPrimitive().getAsString();
                return DateHelper.getDateFromUnixTimestamp(unixTimestamp);
            }
        }).registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {

            @Override
            public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
                return date == null ? null : new JsonPrimitive(DateHelper.dateToISO8601(date));
            }
        }).create();
    }

    public HttpResponse postForm(String uri, Hashtable<String, String> parameters) throws IOException {
        HttpResponse response = null;
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, TIMEOUT_MILISEC);
        HttpConnectionParams.setSoTimeout(myParams, TIMEOUT_MILISEC);
        HttpClient httpClient = new DefaultHttpClient(myParams);
        HttpPost httpPost = new HttpPost(uri);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        httpPost.setHeader("Authorization", basicAuth);
        for (Map.Entry<String, String> a : parameters.entrySet()) {
            urlParameters.add(new BasicNameValuePair(a.getKey(), a.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
            response = httpClient.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return response;
    }

    public HttpResponse call(String uri, String data, HttpMethod method)
            throws IOException {
        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, TIMEOUT_MILISEC);
        HttpConnectionParams.setSoTimeout(myParams, TIMEOUT_MILISEC);
        HttpClient httpClient = new DefaultHttpClient(myParams);
        HttpPut httpPut = null;
        HttpGet httpGet = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        try {
            switch (method) {
                case GET:
                    httpGet = new HttpGet(uri);
                    httpGet.setHeader("Authorization", basicAuth);
                    response = httpClient.execute(httpGet);
                    break;
                case POST:
                    httpPost = new HttpPost(uri);
                    httpPost.setHeader("Authorization", basicAuth);
                    StringEntity se = new StringEntity(data, HTTP.UTF_8);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                    httpPost.setEntity(se);
                    response = httpClient.execute(httpPost);
                    break;
                case PUT:
                    httpPut = new HttpPut(uri);
                    StringEntity se2 = new StringEntity(data, HTTP.UTF_8);
                    se2.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
                    httpPut.setEntity(se2);
                    httpPut.setHeader("Authorization", basicAuth);
                    response = httpClient.execute(httpPut);
                    break;
            }
            return response;
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    private static String buildBasicAuth(String username, String password) {
        StringBuilder sb = new StringBuilder("Basic ");
        String auth = new String(Base64.encode((username + ":" + password).getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        sb.append(auth);
        return sb.toString();
    }

    protected String getErrorCode(HttpResponse response) {
        Header header = response.getFirstHeader("Error-Id");
        return header == null ? null : header.getValue();
    }
    protected String getErrorMsg(HttpResponse response) {
        Header header = response.getFirstHeader("Error-Message");
        return header == null ? null : header.getValue();
    }
}
