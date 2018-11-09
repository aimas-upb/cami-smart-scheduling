package org.aimas.cami.scheduler.CAMIScheduler.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;
import org.aimas.cami.scheduler.CAMIScheduler.domain.Activity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalActivity;
import org.aimas.cami.scheduler.CAMIScheduler.domain.NormalRelativeActivity;
import org.aimas.cami.scheduler.CAMIScheduler.marshal.ChangedActivities;
import org.aimas.cami.scheduler.CAMIScheduler.marshal.ChangedActivity;
import org.aimas.cami.scheduler.CAMIScheduler.utils.PostponeUtils;
import org.aimas.cami.scheduler.CAMIScheduler.utils.Utility;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigPostponeActivity extends RouterConfigImplementation {

	private CAMITaskSchedulerApp camiTaskSchedulerApp;
	private Vertx vertx;
	private PostponeUtils postponeUtils;

	public RouterConfigPostponeActivity(CAMITaskSchedulerApp camiTaskSchedulerApp, Vertx vertx) {
		super();
		this.camiTaskSchedulerApp = camiTaskSchedulerApp;
		this.vertx = vertx;
		postponeUtils = new PostponeUtils<>();
	}

	public void postponeActivity(RoutingContext routingContext) {

		HttpServerResponse response = routingContext.response();

		// handle postponing an activity to the activitySchedule

		// get the json object from client
		String jsonPostpone = routingContext.getBodyAsString();

		System.out.println("Handling \"postponeActivity\"!");

		if (camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().isEmpty()) {
			System.out.println("There is no solution generated, yet.");
			return;
		}

		File solvedSchedule = camiTaskSchedulerApp.getSolutionBusiness().getSolvedFileList().get(0);

		// if there is no working solution in memory, get the last changes from file
		if (camiTaskSchedulerApp.getSolutionBusiness().getSolution() == null)
			camiTaskSchedulerApp.getProblemSolver().openSolution(solvedSchedule);

		// make a deepcopy of the activity list
		List<Activity> beforePostponeActivityList = new ArrayList<>();
		for (Activity activity : camiTaskSchedulerApp.getSolutionBusiness().getSolution().getActivityList())
			if (activity instanceof NormalActivity)
				beforePostponeActivityList.add(((NormalActivity) activity).getNewCopy());
			else
				beforePostponeActivityList.add(((NormalRelativeActivity) activity).getNewCopy());

		vertx.executeBlocking(future -> {

			// postpone the activity
			postponeUtils.postponeActivity(camiTaskSchedulerApp.getSolutionBusiness(), jsonPostpone,
					camiTaskSchedulerApp.getProblemSolver());

			// wait on solving
			synchronized (camiTaskSchedulerApp.getProblemSolver()) {
				try {
					camiTaskSchedulerApp.getProblemSolver().wait();
				} catch (InterruptedException e) {
					// happens if someone interrupts your thread
				}
			}

			// save changed solution to file
			if (solvedSchedule != null)
				camiTaskSchedulerApp.getSolutionBusiness().saveSolution(solvedSchedule);

			// get the new solution
			List<Activity> afterPostponeActivityList = camiTaskSchedulerApp.getSolutionBusiness().getSolution()
					.getActivityList();

			// get the modified activities(activity names)
			List<ChangedActivity> changedActivities = Utility.getChangedActivites(beforePostponeActivityList,
					afterPostponeActivityList);

			System.out
					.println("How many activities have modified? Answer: " + changedActivities.size() + " activities.");

			// send the response(modified activities) back to client
			response.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(new ChangedActivities(changedActivities.size(), changedActivities, 0)));

		}, handler -> {
			System.out.println("The result is: " + handler.result());
		});
	}

}
