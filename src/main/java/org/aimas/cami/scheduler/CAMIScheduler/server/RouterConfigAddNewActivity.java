package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;

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
		String xmlActivity = routingContext.getBodyAsString();

		System.out.println("Handling \"putNewActivity\"!");

		//System.out.println(camiTaskSchedulerApp.getSolutionBusiness() == null);
		response.end("Handling \"putNewActivity\" has ended!");
	}
}
