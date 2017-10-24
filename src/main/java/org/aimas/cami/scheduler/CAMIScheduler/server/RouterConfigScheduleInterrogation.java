package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RouterConfigScheduleInterrogation extends RouterConfigImplementation {

	private ActivitySchedule activitySchedule;

	public RouterConfigScheduleInterrogation(ActivitySchedule activitySchedule) {
		super();
		this.activitySchedule = activitySchedule;
	}

	public void putNewActivity(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		// handle adding a new activity to the activitySchedule
	}
}
