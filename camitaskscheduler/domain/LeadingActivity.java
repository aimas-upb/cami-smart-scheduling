package org.optaplanner.examples.camitaskscheduler.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("LeadingActivity")
public class LeadingActivity extends Activity {

	private FollowingActivity followingActivity;
	private ActivityPeriod delayPeriod;

	public FollowingActivity getFollowingActivity() {
		return followingActivity;
	}

	public void setFollowingActivity(FollowingActivity followingActivity) {
		this.followingActivity = followingActivity;
	}

	@PlanningVariable
	public ActivityPeriod getDelayPeriod() {
		return delayPeriod;
	}

	public void setDelayPeriod(ActivityPeriod delayPeriod) {
		this.delayPeriod = delayPeriod;
	}

	@Override
	public String toString() {
		return "LeadingActivity [followingActivity=" + followingActivity + ", delayPeriod=" + delayPeriod + "]";
	}

}
