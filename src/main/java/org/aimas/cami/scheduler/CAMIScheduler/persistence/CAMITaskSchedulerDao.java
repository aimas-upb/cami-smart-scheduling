package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.XStreamSolutionDao;

public class CAMITaskSchedulerDao extends XStreamSolutionDao<ActivitySchedule> {

	public CAMITaskSchedulerDao() {
		super("activityschedule", ActivitySchedule.class);
	}

}
