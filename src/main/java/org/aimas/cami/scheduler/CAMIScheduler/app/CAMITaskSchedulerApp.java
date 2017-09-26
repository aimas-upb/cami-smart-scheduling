package org.aimas.cami.scheduler.CAMIScheduler.app;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.persistence.CAMITaskSchedulerDao;
import org.aimas.cami.scheduler.CAMIScheduler.swingui.CAMITaskSchedulerPanel;
import org.aimas.cami.scheduler.CAMIScheduler.swingui.SolutionPanel;
import org.aimas.cami.scheduler.CAMIScheduler.utils.CommonApp;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionDao;

/**
 * Main class used to run the application.
 * 
 * @author Bogdan
 *
 */
public class CAMITaskSchedulerApp extends CommonApp<ActivitySchedule> {

	public static final String SOLVER_CONFIG = "solver/CAMITaskSchedulerSolverConfig.xml";

	public static void main(String[] args) {
		prepareSwingEnvironment();
		new CAMITaskSchedulerApp().init();
	}

	public CAMITaskSchedulerApp() {
		super("Weekly Scheduler [CAMI]", "Schedule daily activities every week based on some " + "imposed constraints.",
				SOLVER_CONFIG, null); // no logo
	}

	/**
	 * I/O serializer.
	 */
	@Override
	protected SolutionDao createSolutionDao() {
		return new CAMITaskSchedulerDao();
	}

	/**
	 * Create the GUI.
	 */
	@Override
	protected SolutionPanel<ActivitySchedule> createSolutionPanel() {
		return new CAMITaskSchedulerPanel();
	}

}
