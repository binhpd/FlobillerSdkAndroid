package com.flocash.core.service.entity;

import com.flocash.core.models.CustomField;

import java.io.Serializable;
import java.util.List;

public class AdditionInfo implements Serializable {
	private List<CustomField> fields;

	public List<CustomField> getFields() {
		return fields;
	}

	public void setFields(List<CustomField> fields) {
		this.fields = fields;
	}

}
