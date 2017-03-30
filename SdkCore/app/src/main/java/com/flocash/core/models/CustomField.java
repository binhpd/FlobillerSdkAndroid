package com.flocash.core.models;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class CustomField  implements Serializable {
	public static enum Type {
		NUMBER, DIGIT, CHAR, DATE
	}

	private String name;
	private String displayName;
	private Type type;
	private Integer maxlength;
	private Integer minLength;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

}
