package com.flocash.core.service.entity;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Created by lion on 8/2/16.
 */
@Keep
public class CardInfo implements Serializable {
    private String cardHolder;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvv;


    public CardInfo(String cardHolder, String cardNumber, String expireMonth, String expireYear, String cvv) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvv = cvv;
    }


    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
