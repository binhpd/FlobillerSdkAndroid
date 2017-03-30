package com.flocash.core.service.entity;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
@Keep
public class OrderInfo  implements Serializable {
	private BigDecimal amount;
	private Date orderDate;
	private String currency;
	private String custom;
	private String item_name;
	private String item_price;
	private String quantity;
	private String orderId;
	private String tracking;
	private String traceNumber;
	private String partnerMessage;
	private String status;
	private String statusDesc;
	private String partnerTxn;
	private String cardNumber;
	private String expireMonth;
	private String expireYear;
	private String cvv;
	private String otp;
	private String paymentChannel;
	private PayerInfo payer;
	private UrlInfo url;
	private AdditionInfo additionFields;

	public AdditionInfo getAdditionFields() {
		return additionFields;
	}

	public void setAdditionFields(AdditionInfo additionFields) {
		this.additionFields = additionFields;
	}

	private String instruction;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public UrlInfo getUrl() {
		return url;
	}

	public void setUrl(UrlInfo url) {
		this.url = url;
	}

	public PayerInfo getPayer() {
		return payer;
	}

	public void setPayer(PayerInfo payer) {
		this.payer = payer;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPartnerTxn() {
		return partnerTxn;
	}

	public void setPartnerTxn(String partnerTxn) {
		this.partnerTxn = partnerTxn;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	private UrlInfo redirect;

	public UrlInfo getRedirect() {
		return redirect;
	}

	public void setRedirect(UrlInfo redirect) {
		this.redirect = redirect;
	}

	public String getPartnerMessage() {
		return partnerMessage;
	}

	public void setPartnerMessage(String partnerMessage) {
		this.partnerMessage = partnerMessage;
	}

	private HashMap<String, String> extraPrams;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_price() {
		return item_price;
	}

	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTracking() {
		return tracking;
	}

	public void setTracking(String tracking) {
		this.tracking = tracking;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public HashMap<String, String> getExtraPrams() {
		return extraPrams;
	}

	public void setExtraPrams(HashMap<String, String> extraPrams) {
		this.extraPrams = extraPrams;
	}

}
