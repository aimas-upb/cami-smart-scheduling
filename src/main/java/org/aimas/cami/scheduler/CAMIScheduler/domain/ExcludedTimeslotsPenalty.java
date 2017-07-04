package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * pentru timeslot exclus de forma e.g. "dupa ora 4", as putea seta o valoare
 * mare pentru ora(e.g. ora 24), pentru capatul max, aici, in metoda, sau in XML
 * input
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("ExcludedTimeslots")
public class ExcludedTimeslotsPenalty extends AbstractPersistable {

	private ActivityType activityType;
	private List<ActivityPeriod> excludedActivityPeriods;

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public List<ActivityPeriod> getExcludedActivityPeriods() {
		return excludedActivityPeriods;
	}

	public void setExcludedActivityPeriods(List<ActivityPeriod> excludedActivityPeriods) {
		this.excludedActivityPeriods = excludedActivityPeriods;
	}

}
