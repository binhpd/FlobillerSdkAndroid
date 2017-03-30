package com.flocash.core.service.entity;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class MerchantInfo  implements Serializable {
	private String urlReturn;
	private String urlCancel;
	private String urlLogo;
	private String merchantName;
	private String merchantAccount;

	public String getUrlReturn() {
		return urlReturn;
	}

	public void setUrlReturn(String urlReturn) {
		this.urlReturn = urlReturn;
	}

	public String getUrlCancel() {
		return urlCancel;
	}

	public void setUrlCancel(String urlCancel) {
		this.urlCancel = urlCancel;
	}

	public String getUrlLogo() {
		return urlLogo;
	}

	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantAccount() {
		return merchantAccount;
	}

	public void setMerchantAccount(String merchantAccount) {
		this.merchantAccount = merchantAccount;
	}

}
