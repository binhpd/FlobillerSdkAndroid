package com.flocash.core.models;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

@Keep
public class PaymentOptionInfo  implements Serializable {

	private List<PaymentMethodInfo> banks;
	private List<PaymentMethodInfo> cards;
	private List<PaymentMethodInfo> mobiles;
	private List<PaymentMethodInfo> keyinCards;


	public List<PaymentMethodInfo> getKeyinCards() {
		return keyinCards;
	}

	public void setKeyinCards(List<PaymentMethodInfo> keyinCards) {
		this.keyinCards = keyinCards;
	}
	public List<PaymentMethodInfo> getBanks() {
		return banks;
	}

	public void setBanks(List<PaymentMethodInfo> banks) {
		this.banks = banks;
	}

	public List<PaymentMethodInfo> getCards() {
		return cards;
	}

	public void setCards(List<PaymentMethodInfo> cards) {
		this.cards = cards;
	}

	public List<PaymentMethodInfo> getMobiles() {
		return mobiles;
	}

	public void setMobiles(List<PaymentMethodInfo> mobiles) {
		this.mobiles = mobiles;
	}

}
