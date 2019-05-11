package org.aimas.cami.scheduler.CAMIScheduler.webpage;

import org.aimas.cami.scheduler.CAMIScheduler.server.Runner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class Client extends AbstractVerticle {

	private final static String SERVER_HOST = "cami.alexawada.com";

	public static void main(String[] args) {
		runClient();
	}

	public static void runClient() {
		Runner.runExample(Client.class);
	}

	@Override
	public void start() throws Exception {
		HttpClientOptions options = new HttpClientOptions().setDefaultHost(SERVER_HOST);
		HttpClient client = vertx.createHttpClient(options);

		getUserActivities(client);

	}

	private void getUserActivities(HttpClient client) {

		client.get(SERVER_HOST, "/pernew.php?link=1", response -> {

			System.out.println("Received response with status code " + response.statusCode());

			response.bodyHandler(new Handler<Buffer>() {

				@Override
				public void handle(Buffer event) {
					// System.out.println("Got the event: " + event);

					Object obj;
					try {
						obj = new JSONParser().parse(event.toString());
						JSONObject jsonObject = (JSONObject) obj;
						System.out.println(jsonObject);

						JSONObject custom_fields = (JSONObject) jsonObject.get("s2member_custom_fields");
						String med = (String) custom_fields.get("medlb1");
						System.out.println(med);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}
			});
		}).end();

	}

}
