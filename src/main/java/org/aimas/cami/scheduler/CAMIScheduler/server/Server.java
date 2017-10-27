package org.aimas.cami.scheduler.CAMIScheduler.server;

import org.aimas.cami.scheduler.CAMIScheduler.app.CAMITaskSchedulerApp;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

	private Router router;
	private final static int SERVER_PORT = 8080;
	private final static String SERVER_HOST = "localhost";

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}

	@Override
	public void start() {

		// init the app
		CAMITaskSchedulerApp camiTaskSchedulerApp = new CAMITaskSchedulerApp();

		// create a new empty router
		RouterConfig routerConfig = new RouterConfigImplementation();
		router = routerConfig.createRouterNewActivity(vertx, camiTaskSchedulerApp);

		vertx.createHttpServer().requestHandler(router::accept).listen(SERVER_PORT, SERVER_HOST, res -> {
			if (res.succeeded()) {
				System.out.println("Server is now listening!");
				camiTaskSchedulerApp.runApp();
			} else {
				System.out.println("Failed to bind!");
			}
		});
	}

}
