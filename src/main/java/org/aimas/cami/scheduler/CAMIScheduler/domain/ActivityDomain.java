package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * 
 * @author Bogdan
 *
 */
public class ActivityDomain extends AbstractPersistable {

	@JsonManagedReference
	private List<ActivityCategory> categories;

	private String code;
	private String description;
	private int index;

	public ActivityDomain() {
	}

	public ActivityDomain(String code) {
		super();
		this.code = code;
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

	public List<ActivityCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ActivityCategory> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "ActivityCategory [code=" + code + ", description=" + description + "]";
	}

}
