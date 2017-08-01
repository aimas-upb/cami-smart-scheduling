package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity
@XStreamAlias("RelativeActivity")
public class RelativeActivity extends Activity {

	private int offset; // in minutes

	@PlanningVariable(valueRangeProviderRefs = {
			"activityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	@Override
	public ActivityPeriod getActivityPeriod() {
		return super.getActivityPeriod();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Time getRelativeActivityPeriodTime() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getTime();
	}

	public WeekDay getRelativeActivityWeekDay() {
		if (activityPeriod == null)
			return null;
		return activityPeriod.getWeekDay();
	}

	@Override
	public String toString() {
		return "RelativeActivity [activityType=" + getActivityType() + ", activityPeriod=" + getActivityPeriod()
				+ ", offset=" + offset + "]";
	}

}
