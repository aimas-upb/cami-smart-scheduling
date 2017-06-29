package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RelativeActivityPenalty")
public class RelativeActivityPenalty extends AbstractPersistable {

	private RelativeType relativeType;
	private ActivityType firstActivity, secondActivity;

	public RelativeType getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(RelativeType relativeType) {
		this.relativeType = relativeType;
	}

	public ActivityType getFirstActivity() {
		return firstActivity;
	}

	public void setFirstActivity(ActivityType firstActivity) {
		this.firstActivity = firstActivity;
	}

	public ActivityType getSecondActivity() {
		return secondActivity;
	}

	public void setSecondActivity(ActivityType secondActivity) {
		this.secondActivity = secondActivity;
	}

	@Override
	public String toString() {
		return "RelativeActivityPenalty [relativeType=" + relativeType + ", firstActivity=" + firstActivity
				+ ", secondActivity=" + secondActivity + "]";
	}

}
