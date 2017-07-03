package org.optaplanner.examples.camitaskscheduler.domain;

import org.optaplanner.examples.common.domain.AbstractPersistable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ActivityCategory")
public class ActivityCategory extends AbstractPersistable {

	private ActivitySubcategory subcategory;
	private String code;
	private String description;
	private int index;

	public ActivitySubcategory getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(ActivitySubcategory subcategory) {
		this.subcategory = subcategory;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "ActivityCategory [code=" + code + ", description=" + description + "]";
	}

}
