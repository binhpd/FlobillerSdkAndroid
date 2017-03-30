package com.flocash.core.service.entity;

import android.support.annotation.Keep;

import com.flocash.core.models.PaymentMethodInfo;

import java.io.Serializable;

/**
 * Created by ThaiThinh on 6/13/2015.
 */
@Keep
public class Request  implements Serializable {
    private OrderInfo order;
    private PayerInfo payer;
    private MerchantInfo merchant;
    private PaymentMethodInfo payOption;
    private CardInfo cardInfo;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public PayerInfo getPayer() {
        return payer;
    }

    public void setPayer(PayerInfo payer) {
        this.payer = payer;
    }

    public MerchantInfo getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantInfo merchant) {
        this.merchant = merchant;
    }

    public PaymentMethodInfo getPayOption() {
        return payOption;
    }

    public void setPayOption(PaymentMethodInfo payOption) {
        this.payOption = payOption;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
}
