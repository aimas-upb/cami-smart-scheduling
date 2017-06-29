package org.aimas.cami.scheduler.CAMIScheduler.domain;

import org.aimas.cami.scheduler.CAMIScheduler.domain.solver.LeadingActivityPeriodUpdateListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("LeadingActivity")
public class LeadingActivity extends Activity {

	// shadow variable
	private ActivityPeriod leadingActivityPeriod;
	private int offset;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	@CustomShadowVariable(variableListenerClass = LeadingActivityPeriodUpdateListener.class, sources = {
			@PlanningVariableReference(entityClass = FollowingActivity.class, variableName = "advanceperiod") })
	public ActivityPeriod getLeadingActivityPeriod() {
		return leadingActivityPeriod;
	}

	public void setLeadingActivityPeriod(ActivityPeriod leadingActivityPeriod) {
		this.leadingActivityPeriod = leadingActivityPeriod;
	}

}
