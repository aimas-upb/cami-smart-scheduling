package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.domain.RelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Time;
import org.aimas.cami.scheduler.CAMIScheduler.domain.TimeInterval;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AdjustActivityPeriod;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

/**
 * Generate moves for relative activities.
 * 
 * @author Bogdan
 *
 */
public class RelativeActivityPeriodMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<RelativeActivityPeriodMove> createMoveList(ActivitySchedule activitySchedule) {
		List<RelativeActivityPeriodMove> moveList = new ArrayList<>();
		List<Activity> activityList = activitySchedule.getActivityList();

		for (Activity activity : activityList) {
			if (activity instanceof RelativeActivity) {
				int minutesInInterval;
				int offset = ((RelativeActivity) activity).getOffset();

				if (offset < 0) {
					minutesInInterval = -60;

					for (int i = 0; i > minutesInInterval; i--)
						moveList.add(new RelativeActivityPeriodMove((RelativeActivity) activity, i));
				} else if (offset > 0) {
					minutesInInterval = 60;

					if (((RelativeActivity) activity).getActivityPeriodTime() != null
							&& ((RelativeActivity) activity).getActivityPeriodTime().getHour() < 23)
						for (int i = 0; i < minutesInInterval; i++)
							moveList.add(new RelativeActivityPeriodMove((RelativeActivity) activity, i));
				}

			}
		}

		return moveList;
	}

}
