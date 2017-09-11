package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.RelativeActivityPeriodUpdateListener;
import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity
@XStreamAlias("RelativeActivity")
public class RelativeActivity extends Activity {

	// shadow variable (it depends on a planning variable)
	// if the planning variable changes, then the shadow variable adjusts
	// according to it
	private ActivityPeriod relativeActivityPeriod;

	private int offset; // in minutes
	private boolean assigned;

	@PlanningVariable(valueRangeProviderRefs = {
			"activityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	@Override
	public ActivityPeriod getActivityPeriod() {
		return super.getActivityPeriod();
	}

	@CustomShadowVariable(variableListenerClass = RelativeActivityPeriodUpdateListener.class, sources = {
			@PlanningVariableReference(entityClass = Activity.class, variableName = "activityPeriod") })
	public ActivityPeriod getRelativeActivityPeriod() {
		return relativeActivityPeriod;
	}

	public void setRelativeActivityPeriod(ActivityPeriod relativeActivityPeriod) {
		this.relativeActivityPeriod = relativeActivityPeriod;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}

	public Time getRelativeActivityPeriodTime() {
		if (relativeActivityPeriod == null)
			return null;
		return relativeActivityPeriod.getTime();
	}

	public WeekDay getRelativeActivityWeekDay() {
		if (relativeActivityPeriod == null)
			return null;
		return relativeActivityPeriod.getWeekDay();
	}

	public ActivityPeriod getRelativeActivityEndPeriod() {
		if (relativeActivityPeriod == null) {
			return null;
		}
		return AdjustActivityPeriod.getAdjustedPeriod(relativeActivityPeriod, getActivityType().getDuration());
	}

	@Override
	public String toString() {
		return "RelativeActivity [activityType=" + getActivityType() + ", activityPeriod=" + relativeActivityPeriod
				+ ", offset=" + offset + "]";
	}

}
