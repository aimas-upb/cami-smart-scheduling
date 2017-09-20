package org.aimas.cami.scheduler.CAMIScheduler.domain;

import java.util.List;
import java.util.Map;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.TimeWeightFactory;
import org.aimas.cami.scheduler.CAMIScheduler.postpone.Postpone;
import org.aimas.cami.scheduler.CAMIScheduler.solver.move.MovableActivitySelectionFilter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * 
 * @author Bogdan
 *
 */
@PlanningEntity(movableEntitySelectionFilter = MovableActivitySelectionFilter.class)
@XStreamAlias("NormalActivity")
public class NormalActivity extends Activity {

	// planning variable, changes during planning
	protected ActivityPeriod activityPeriod;

	Map<String, Long> assignedToRelativeActivityMap;
	List<ActivityPeriod> periodDomainRangeList;

	@Override
	@PlanningVariable(valueRangeProviderRefs = {
			"activityPeriodRange" }, strengthWeightFactoryClass = TimeWeightFactory.class)
	public ActivityPeriod getActivityPeriod() {
		return activityPeriod;
	}

	public void setActivityPeriod(ActivityPeriod activityPeriod) {
		this.activityPeriod = activityPeriod;
	}

	@ValueRangeProvider(id = "activityPeriodRange")
	public List<ActivityPeriod> getPeriodDomainRangeList() {
		return periodDomainRangeList;
	}

	public void setPeriodDomainRangeList(List<ActivityPeriod> periodDomainRangeList) {
		this.periodDomainRangeList = periodDomainRangeList;
	}

	public Map<String, Long> getAssignedToRelativeActivityMap() {
		return assignedToRelativeActivityMap;
	}

	public void setAssignedToRelativeActivityMap(Map<String, Long> assignedRelativeActivityList) {
		this.assignedToRelativeActivityMap = assignedRelativeActivityList;
	}

	@Override
	public String toString() {
		return "Activity [activityType=" + getActivityType() + ", activityPeriod=" + activityPeriod + "]";
	}

}