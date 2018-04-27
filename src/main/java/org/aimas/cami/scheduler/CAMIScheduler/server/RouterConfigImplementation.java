package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class RouterConfigImplementation extends RouterConfig {

	@Override
	public Router createRouterNewActivity(Vertx vertx, CAMITaskSchedulerApp camiTaskSchedulerApp) {
		Router router = this.createRouter(vertx);
		RouterConfigAddNewActivity addNewActivityRoute = new RouterConfigAddNewActivity(vertx, camiTaskSchedulerApp);
		RouterConfigActivitySchedule activityScheduleRoute = new RouterConfigActivitySchedule(vertx,
				camiTaskSchedulerApp);

		// add a 'add a new activity' route to router
		router.post(RoutePaths.API_ROUTE + RoutePaths.NEW_ACTIVITY_ROUTE).handler(addNewActivityRoute::putNewActivity);
		router.get(RoutePaths.API_ROUTE + RoutePaths.ACTIVITY_SCHEDULE_ROUTE)
				.handler(activityScheduleRoute::getActivitySchedule);

		return router;
	}

}
