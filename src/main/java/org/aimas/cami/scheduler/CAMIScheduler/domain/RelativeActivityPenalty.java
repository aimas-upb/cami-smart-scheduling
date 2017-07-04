package org.aimas.cami.scheduler.CAMIScheduler.domain;

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
	private ActivityType firstActivityType, secondActivityType;

	public RelativeType getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(RelativeType relativeType) {
		this.relativeType = relativeType;
	}

	public ActivityType getFirstActivityType() {
		return firstActivityType;
	}

	public void setFirstActivityType(ActivityType firstActivityType) {
		this.firstActivityType = firstActivityType;
	}

	public ActivityType getSecondActivityType() {
		return secondActivityType;
	}

	public void setSecondActivityType(ActivityType secondActivityType) {
		this.secondActivityType = secondActivityType;
	}

	@Override
	public String toString() {
		return "RelativeActivityPenalty [relativeType=" + relativeType + ", firstActivityType=" + firstActivityType
				+ ", secondActivityType=" + secondActivityType + "]";
	}

}
