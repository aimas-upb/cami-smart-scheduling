package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigDeleteActivity extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;

	public RouterConfigDeleteActivity(CAMITaskSchedulerApp camiTaskSchedulerApp) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
	}

	public void deleteActivity(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();

		System.out.println("Handling \"deleteActivity\"!");

		if (camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().isEmpty()) {
			System.out.println("There is no solution generated, yet.");
			return;
		}

		File solvedSchedule = camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().get(0);
		camiTaskSchedulerApp.getSolutionBusiness().openSolution(solvedSchedule);

		// just to confirm(GUI) that the solution was loaded
		camiTaskSchedulerApp.getSolverAndPersistenceFrame().loadSolution();

		// send the response(modified activities) back to client
		response.putHeader("content-type", "application/json; charset=utf-8").end();
	}

}
