package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionUtils;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigDeleteActivity extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;
	private SolutionUtils solutionUtils;

	public RouterConfigDeleteActivity(CAMITaskSchedulerApp camiTaskSchedulerApp) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
		solutionUtils = new SolutionUtils<>();
	}

	public void deleteActivity(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();

		// get the activity that has to be deleted
		String xmlActivity = routingContext.getBodyAsString();

		System.out.println("Handling \"deleteActivity\"!");

		if (camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().isEmpty()) {
			System.out.println("There is no solution generated, yet.");
			return;
		}

		File solvedSchedule = camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().get(0);
		camiTaskSchedulerApp.getSolutionBusiness().openSolution(solvedSchedule);

		// just to confirm(GUI) that the solution was loaded
		camiTaskSchedulerApp.getSolverAndPersistenceFrame().loadSolution();

		solutionUtils.deleteActivityFromSchedule(camiTaskSchedulerApp.getSolutionBusiness(), xmlActivity);

		// send empty response back to client
		response.end();

		// visual proof of delete action
		camiTaskSchedulerApp.getSolverAndPersistenceFrame().resetScreen();

		// save changed solution
		camiTaskSchedulerApp.getSolutionBusiness().saveSolution(solvedSchedule);
	}

}
