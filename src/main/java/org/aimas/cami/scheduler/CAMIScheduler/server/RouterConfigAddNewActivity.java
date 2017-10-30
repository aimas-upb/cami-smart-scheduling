package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigAddNewActivity extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;

	public RouterConfigAddNewActivity(CAMITaskSchedulerApp camiTaskSchedulerApp) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
	}

	public void putNewActivity(RoutingContext routingContext) {

		HttpServerResponse response = routingContext.response();

		// handle adding a new activity to the activitySchedule

		// get the activity from client
		String xmlActivity = routingContext.getBodyAsString();

		// System.out.println("Got from client:\n" + xmlActivity);

		System.out.println("Handling \"putNewActivity\"!");

		if (camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().isEmpty()) {
			System.out.println("There is no solution generated, yet.");
			return;
		}

		// get first solution
		File solvedSchedule = camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().get(0);
		camiTaskSchedulerApp.getSolutionBusiness().openSolution(solvedSchedule);

		// just to confirm(GUI) that the solution was loaded
		camiTaskSchedulerApp.getSolverAndPersistenceFrame().setSolutionLoaded();
		
		// deserialize the XML String

		// System.out.println(camiTaskSchedulerApp.getSolutionBusiness() == null);
		response.end("Handling \"putNewActivity\" has ended!");
	}
}
