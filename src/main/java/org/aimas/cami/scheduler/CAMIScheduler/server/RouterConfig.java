package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public abstract class RouterConfig {

	public static final String API_ROUTE = "/api";
	public static final String SCHEDULE_INTERROGATION_ROUTE = "/interrogation";

	public abstract Router createRouterNewActivity(Vertx vertx, ActivitySchedule activitySchedule);

	protected Router createRouter(Vertx vertx) {
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		return router;
	}

}
