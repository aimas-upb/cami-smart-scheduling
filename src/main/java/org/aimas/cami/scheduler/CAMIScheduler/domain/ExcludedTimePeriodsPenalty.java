package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * pentru timeslot exclus de forma e.g. "dupa ora 4", as putea seta o valoare
 * mare pentru ora(e.g. ora 24), pentru capatul max
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("ExcludedTimePeriodsPenalty")
public class ExcludedTimePeriodsPenalty extends AbstractPersistable {

	private ActivityType activityType;
	private List<PeriodInterval> excludedActivityPeriods;

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public List<PeriodInterval> getExcludedActivityPeriods() {
		return excludedActivityPeriods;
	}

	public void setExcludedActivityPeriods(List<PeriodInterval> excludedActivityPeriods) {
		this.excludedActivityPeriods = excludedActivityPeriods;
	}

	@Override
	public String toString() {
		return "ExcludedTimePeriodsPenalty [activityType=" + activityType + ", excludedActivityPeriods="
				+ excludedActivityPeriods + "]";
	}

}
