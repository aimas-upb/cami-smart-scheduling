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
	private ActivityType firstActivityType, secondActivityType;
	
	// useful for activities that are relative to an activity OR another, preset
	private List<ActivityType> disjunctiveActivities;

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

	public List<ActivityType> getDisjunctiveActivities() {
		return disjunctiveActivities;
	}

	public void setDisjunctiveActivities(List<ActivityType> disjunctiveActivities) {
		this.disjunctiveActivities = disjunctiveActivities;
	}

	@Override
	public String toString() {
		return "RelativeActivityPenalty [relativeType=" + relativeType + ", firstActivityType=" + firstActivityType
				+ ", secondActivityType=" + secondActivityType + "]";
	}

}
