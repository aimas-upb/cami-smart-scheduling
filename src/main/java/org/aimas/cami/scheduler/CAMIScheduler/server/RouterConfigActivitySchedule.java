package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigActivitySchedule extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;

	public RouterConfigActivitySchedule(Vertx vertx, CAMITaskSchedulerApp camiTaskSchedulerApp) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
	}

	public void getActivitySchedule(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();

		System.out.println("Handling \"getActivitySchedule\"!");

		if (camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().isEmpty()) {
			System.out.println("There is no solution generated, yet.");
			return;
		}

		File solvedSchedule = camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().get(0);
		camiTaskSchedulerApp.getSolutionBusiness().openSolution(solvedSchedule);

		// just to confirm(GUI) that the solution was loaded
		camiTaskSchedulerApp.getSolverAndPersistenceFrame().loadSolution();

		List<Activity> activityList = camiTaskSchedulerApp.getSolutionBusiness().getSolution().getActivityList();

		StringBuilder activitySchedule = new StringBuilder();

		for (Activity activity : activityList) {
			activitySchedule.append("Activity ["
					+ (activity.getActivityTypeCode() + "] | " + activity.getActivityPeriodWeekday().toString() + " | "
							+ activity.getActivityPeriodTime().toString())
					+ " || \n");
		}

		// send the response(modified activities) back to client
		response.end(activitySchedule.toString());
	}
}
