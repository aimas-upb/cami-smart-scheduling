package org.aimas.cami.scheduler.CAMIScheduler.marshal;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractPersistable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Postpone a set of activities from the schedule.
 * 
 * @author Bogdan
 *
 */
@XStreamAlias("PostponedActivities")
public class PostponedActivities extends AbstractPersistable {
	private List<PostponedActivity> postponedActivities;

	public PostponedActivities(List<PostponedActivity> postponedActivities) {
		super();
		this.postponedActivities = postponedActivities;
	}

	public List<PostponedActivity> getPostponeActivities() {
		return postponedActivities;
	}

	public void setPostponeActivities(List<PostponedActivity> postponedActivities) {
		this.postponedActivities = postponedActivities;
	}

}
