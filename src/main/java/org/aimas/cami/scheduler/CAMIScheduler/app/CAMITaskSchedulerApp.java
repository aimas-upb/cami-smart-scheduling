package org.aimas.cami.scheduler.CAMIScheduler.app;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.persistence.CAMITaskSchedulerDao;
import org.aimas.cami.scheduler.CAMIScheduler.persistence.CAMITaskSchedulerExporter;
import org.aimas.cami.scheduler.CAMIScheduler.persistence.CAMITaskSchedulerImporter;
import org.aimas.cami.scheduler.CAMIScheduler.swingui.CAMITaskSchedulerPanel;
import org.aimas.cami.scheduler.CAMIScheduler.swingui.SolutionPanel;
import org.aimas.cami.scheduler.CAMIScheduler.utils.CommonApp;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractSolutionExporter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.AbstractSolutionImporter;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionDao;

public class CAMITaskSchedulerApp extends CommonApp<ActivitySchedule> {

	public static final String SOLVER_CONFIG = "solver/CAMITaskSchedulerSolverConfig.xml";

	public static void main(String[] args) {
		prepareSwingEnvironment();
		new CAMITaskSchedulerApp().init();
	}

	public CAMITaskSchedulerApp() {
		super("Weekly Scheduler [CAMI]", "Schedule daily activities every week based on some " + "imposed constraints.",
				SOLVER_CONFIG, null); // no logo selected
	}

	@Override
	protected SolutionDao createSolutionDao() {
		return new CAMITaskSchedulerDao();
	}

	@Override
	protected AbstractSolutionExporter createSolutionExporter() {
		return new CAMITaskSchedulerExporter();
	}

	@Override
	protected AbstractSolutionImporter[] createSolutionImporters() {
		return new AbstractSolutionImporter[] { new CAMITaskSchedulerImporter() };
	}

	@Override
	protected SolutionPanel<ActivitySchedule> createSolutionPanel() {
		return new CAMITaskSchedulerPanel();
	}

}
