package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("FollowingActivity")
public class FollowingActivity extends Activity {

	private ActivityPeriod advancePeriod;

	@PlanningVariable
	public ActivityPeriod getAdvancePeriod() {
		return advancePeriod;
	}

	public void setAdvancePeriod(ActivityPeriod advancePeriod) {
		this.advancePeriod = advancePeriod;
	}

	@Override
	public String toString() {
		return "FollowingActivity [leadingActivity=" + ", advancePeriod=" + advancePeriod + "]";
	}

}
