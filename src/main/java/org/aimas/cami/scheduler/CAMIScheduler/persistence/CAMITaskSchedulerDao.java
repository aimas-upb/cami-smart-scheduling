package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.JSONSolutionDao;

/**
 * 
 * @author Bogdan
 *
 */
public class CAMITaskSchedulerDao extends JSONSolutionDao<ActivitySchedule> {

	public CAMITaskSchedulerDao() {
		super("activityschedule", ActivitySchedule.class);
	}

}
