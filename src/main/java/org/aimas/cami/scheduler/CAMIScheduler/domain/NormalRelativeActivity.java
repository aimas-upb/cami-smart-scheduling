package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.ActivityDifficultyWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * An activity that is planned depending on other's activity period.
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class, difficultyWeightFactoryClass = ActivityDifficultyWeightFactory.class)
@XStreamAlias("NormalRelativeActivity")
public class NormalRelativeActivity extends Activity {

	// shadow variable (it depends on a planning variable)
	// if the planning variable changes, then the shadow variable adjusts
	// according to it
	private ActivityPeriod activityPeriod;

	private int offset; // offset (in minutes) - distance between an activity and an activity relative
						// to it
	private boolean assigned;

	private RelativeType relativeType;

	@Override
	@PlanningVariable(valueRangeProviderRefs = {
			"relativeActivityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	public ActivityPeriod getActivityPeriod() {
		return activityPeriod;
	}

	public void setActivityPeriod(ActivityPeriod activityPeriod) {
		this.activityPeriod = activityPeriod;
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

	public RelativeType getRelativeType() {
		return relativeType;
	}

	public void setRelativeType(RelativeType relativeType) {
		this.relativeType = relativeType;
	}

	@ValueRangeProvider(id = "relativeActivityPeriodRange")
	public List<ActivityPeriod> getPeriodDomainRangeList() {
		return periodDomainRangeList;
	}

	@Override
	public String toString() {
		return "RelativeActivity [activityType=" + getActivityType() + ", activityPeriod=" + activityPeriod
				+ ", offset=" + offset + "]";
	}

	@Override
	public String getLabel() {
		return getActivityTypeCode();
	}

}
