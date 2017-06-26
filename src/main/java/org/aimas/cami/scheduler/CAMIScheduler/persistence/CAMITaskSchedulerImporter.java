package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import java.io.IOException;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractTxtSolutionImporter;

public class CAMITaskSchedulerImporter extends AbstractTxtSolutionImporter<ActivitySchedule> {
	
	public static void main(String[] args) {
		new CAMITaskSchedulerImporter().convertAll();
	}

	public CAMITaskSchedulerImporter() {
		super(new CAMITaskSchedulerDao());
	}

	@Override
	public TxtInputBuilder<ActivitySchedule> createTxtInputBuilder() {
		return null;
	}

	public static class CAMITaskSchedulerInputBuilder extends TxtInputBuilder<ActivitySchedule> {

		@SuppressWarnings("unused")
		@Override
		public ActivitySchedule readSolution() throws IOException {
			// TODO
			ActivitySchedule activitySchedule = new ActivitySchedule();
			return null;
		}

	}

}
