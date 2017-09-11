package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Represent a relativity between a relative activity and a category (e.g. Meal)
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("ActivityRelativeToActivityCategory")
public class ActivityRelativeToActivityCategory extends AbstractPersistable {

	private RelativeType relativeType;
	private ActivityType relativeActivityType;
	String category;

	public RelativeType getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(RelativeType relativeType) {
		this.relativeType = relativeType;
	}

	public ActivityType getRelativeActivityType() {
		return relativeActivityType;
	}

	public void setRelativeActivityType(ActivityType relativeActivityType) {
		this.relativeActivityType = relativeActivityType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ActivityRelativeToActivityCategory [relativeType=" + relativeType + ", relativeActivityType="
				+ relativeActivityType + ", category=" + category + "]";
	}

}
