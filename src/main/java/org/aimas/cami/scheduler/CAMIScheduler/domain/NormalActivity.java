package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;
import java.util.Map;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.ActivityDifficultyWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * An activity to be planned.
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class, difficultyWeightFactoryClass = ActivityDifficultyWeightFactory.class)
@XStreamAlias("NormalActivity")
public class NormalActivity extends Activity {

	// planning variable, changes during planning
	protected ActivityPeriod activityPeriod;

	// map of the relative activities assigned to this activity
	Map<String, Long> assignedToRelativeActivityMap;

	@Override
	@PlanningVariable(valueRangeProviderRefs = {
			"activityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	public ActivityPeriod getActivityPeriod() {
		return activityPeriod;
	}

	@Override
	public void setActivityPeriod(ActivityPeriod activityPeriod) {
		this.activityPeriod = activityPeriod;
	}

	public Map<String, Long> getAssignedToRelativeActivityMap() {
		return assignedToRelativeActivityMap;
	}

	public void setAssignedToRelativeActivityMap(Map<String, Long> assignedRelativeActivityList) {
		this.assignedToRelativeActivityMap = assignedRelativeActivityList;
	}

	@ValueRangeProvider(id = "activityPeriodRange")
	public List<ActivityPeriod> getPeriodDomainRangeList() {
		return periodDomainRangeList;
	}

	@Override
	public String toString() {
		return "Activity [activityType=" + getActivityType() + ", activityPeriod=" + activityPeriod + "]";
	}

	@Override
	public String getLabel() {
		return getActivityTypeCode();
	}

	public Activity getNewCopy() {
		NormalActivity normalActivityCopy = new NormalActivity();

		normalActivityCopy.setId(this.getId());
		normalActivityCopy.setUuid(this.getUuid());
		normalActivityCopy.setActivityType(this.getActivityType());
		normalActivityCopy.setActivityPeriod(this.getActivityPeriod());

		return normalActivityCopy;
	}

}
