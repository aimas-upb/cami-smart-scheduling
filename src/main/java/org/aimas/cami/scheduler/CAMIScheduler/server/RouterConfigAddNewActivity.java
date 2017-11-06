package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionUtils;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigAddNewActivity extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;
	private SolutionUtils solutionUtils;

	public RouterConfigAddNewActivity(CAMITaskSchedulerApp camiTaskSchedulerApp) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
		solutionUtils = new SolutionUtils<>();
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

		List<Activity> beforeAddActivityList = camiTaskSchedulerApp.getSolutionBusiness().getSolution()
				.getActivityList();

		// add the new activity to the schedule
		solutionUtils.addNewActivityFromXML(camiTaskSchedulerApp.getSolutionBusiness(), xmlActivity,
				camiTaskSchedulerApp.getSolverAndPersistenceFrame());

		List<Activity> afterAddActivityList = camiTaskSchedulerApp.getSolutionBusiness().getSolution()
				.getActivityList();

		List<String> changedActivitiesList = solutionUtils.getChangedActivites(beforeAddActivityList,
				afterAddActivityList);

		StringBuilder changedActivities = new StringBuilder();

		for (String activity : changedActivitiesList) {
			changedActivities.append(activity + "||");
		}

		// System.out.println(camiTaskSchedulerApp.getSolutionBusiness() == null);

		// send the response back to client
		response.end(changedActivities.toString());
	}
}
