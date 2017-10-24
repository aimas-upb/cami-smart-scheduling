package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.domain.ActivitySchedule;
import org.aimas.cami.scheduler.CAMIScheduler.utils.SolutionBusiness;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class Server<Solution_> extends AbstractVerticle {

	private Router router;
	private SolutionBusiness<Solution_> solutionBusiness;
	private final static int SERVER_PORT = 8080;
	private final static String SERVER_HOST = "localhost";

	@Override
	public void start() {

		// create a new empty router
		RouterConfig routerConfig = new RouterConfigImplementation();
		router = routerConfig.createRouterNewActivity(vertx, (ActivitySchedule) solutionBusiness.getSolution());

		vertx.createHttpServer().requestHandler(router::accept).listen(SERVER_PORT, SERVER_HOST, res -> {
			if (res.succeeded()) {
				System.out.println("Server is now listening!");
			} else {
				System.out.println("Failed to bind!");
			}
		});
	}

	public void setSolutionBusiness(SolutionBusiness<Solution_> solutionBusiness) {
		this.solutionBusiness = solutionBusiness;
	}

}
