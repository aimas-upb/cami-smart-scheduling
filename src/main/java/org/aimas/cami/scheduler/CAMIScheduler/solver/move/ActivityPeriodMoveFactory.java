package org.aimas.cami.scheduler.CAMIScheduler.solver.move;

import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

/**
 * for now, not sure if needed
 * 
 * @author Bogdan
 *
 */
public class ActivityPeriodMoveFactory implements MoveListFactory<ActivitySchedule> {

	@Override
	public List<? extends Move<ActivitySchedule>> createMoveList(ActivitySchedule solution) {
		// TODO Auto-generated method stub
		return null;
	}

}
