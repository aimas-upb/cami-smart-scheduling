package org.optaplanner.examples.camitaskscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("FollowingActivity")
public class FollowingActivity extends Activity {

	private LeadingActivity leadingActivity;
	private ActivityPeriod advancePeriod;

	public LeadingActivity getLeadingActivity() {
		return leadingActivity;
	}

	public void setLeadingActivity(LeadingActivity leadingActivity) {
		this.leadingActivity = leadingActivity;
	}

	@PlanningVariable
	public ActivityPeriod getAdvancePeriod() {
		return advancePeriod;
	}

	public void setAdvancePeriod(ActivityPeriod advancePeriod) {
		this.advancePeriod = advancePeriod;
	}

	@Override
	public String toString() {
		return "FollowingActivity [leadingActivity=" + leadingActivity + ", advancePeriod=" + advancePeriod + "]";
	}

}
