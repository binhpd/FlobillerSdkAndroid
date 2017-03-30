package com.flocash.core.service.entity;

import java.io.Serializable;
import java.util.HashMap;

public class UrlInfo  implements Serializable {
	public static enum Method {
		post, get;
	}

	private Method method;
	private String url;
	private HashMap<String, String> params;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}

}
