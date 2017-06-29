package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.FollowingActivityPeriodUpdateListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("FollowingActivity")
public class FollowingActivity extends Activity {

	// shadow variable
	private ActivityPeriod followingActivityPeriod;

	private int offset;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@CustomShadowVariable(variableListenerClass = FollowingActivityPeriodUpdateListener.class, sources = {
			@PlanningVariableReference(entityClass = LeadingActivity.class, variableName = "delayperiod") })
	public ActivityPeriod getFollowingActivityPeriod() {
		return followingActivityPeriod;
	}

	public void setFollowingActivityPeriod(ActivityPeriod followingActivityPeriod) {
		this.followingActivityPeriod = followingActivityPeriod;
	}

	@Override
	public String toString() {
		return "FollowingActivity [followingActivityPeriod=" + followingActivityPeriod + "]";
	}

}
