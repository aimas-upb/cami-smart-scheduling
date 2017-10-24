package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class RouterConfigImplementation extends RouterConfig {

	@Override
	public Router createRouterNewActivity(Vertx vertx, ActivitySchedule activitySchedule) {
		Router router = this.createRouter(vertx);
		RouterConfigScheduleInterrogation scheduleInterrogation = new RouterConfigScheduleInterrogation(
				activitySchedule);

		router.put(RouterConfig.API_ROUTE + RouterConfig.SCHEDULE_INTERROGATION_ROUTE)
				.handler(scheduleInterrogation::putNewActivity);
		return router;
	}

}
