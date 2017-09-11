package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("RelativeActivityPenalty")
public class RelativeActivityPenalty extends AbstractPersistable {

	private RelativeType relativeType;
	private String relativeActivityType;
	private String staticActivityType;
	private String category;

	public RelativeType getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(RelativeType relativeType) {
		this.relativeType = relativeType;
	}

	public String getRelativeActivityType() {
		return relativeActivityType;
	}

	public void setRelativeActivityType(String relativeActivityType) {
		this.relativeActivityType = relativeActivityType;
	}

	public String getStaticActivityType() {
		return staticActivityType;
	}

	public void setStaticActivityType(String staticActivityType) {
		this.staticActivityType = staticActivityType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "RelativeActivityPenalty [relativeType=" + relativeType + ", relativeActivityType="
				+ relativeActivityType + ", staticActivityType=" + staticActivityType + "]";
	}

}
