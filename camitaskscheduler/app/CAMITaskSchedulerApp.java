package org.optaplanner.examples.camitaskscheduler.app;

import org.optaplanner.examples.camitaskscheduler.domain.ActivitySchedule;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.persistence.SolutionDao;
import org.optaplanner.examples.common.swingui.SolutionPanel;

public class CAMITaskSchedulerApp extends CommonApp<ActivitySchedule> {

	public static final String SOLVER_CONFIG = "org/optaplanner/examples/curriculumcourse/solver/CAMITaskSchedulerSolverConfig.xml";

	public static void main(String[] args) {
		prepareSwingEnvironment();
		new CAMITaskSchedulerApp().init();
	}

	public CAMITaskSchedulerApp() {
		// TODO Auto-generated constructor stub
		super("CAMIScheduler", "CAMIProject", SOLVER_CONFIG, null);// add logo
	}

	@Override
	protected SolutionPanel<ActivitySchedule> createSolutionPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SolutionDao createSolutionDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
