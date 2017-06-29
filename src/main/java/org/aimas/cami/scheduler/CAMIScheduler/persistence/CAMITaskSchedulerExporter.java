package org.aimas.cami.scheduler.CAMIScheduler.persistence;

import java.io.IOException;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractTxtSolutionExporter;

public class CAMITaskSchedulerExporter extends AbstractTxtSolutionExporter<ActivitySchedule> {

	private static final String OUTPUT_FILE_SUFFIX = "sol";

	public static void main(String[] args) {
		new CAMITaskSchedulerExporter().convertAll();
	}

	public CAMITaskSchedulerExporter() {
		super(new CAMITaskSchedulerDao());
	}

	public String getOutputFileSuffix() {
		return OUTPUT_FILE_SUFFIX;
	}

	@Override
	public TxtOutputBuilder<ActivitySchedule> createTxtOutputBuilder() {
		return new CAMITaskSchedulerOutputBuilder();
	}

	public static class CAMITaskSchedulerOutputBuilder extends TxtOutputBuilder<ActivitySchedule> {

		@Override
		public void writeSolution() throws IOException {
			// TODO
		}

	}

}
