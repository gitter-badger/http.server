package black.door.net.http.server.example;

import black.door.net.http.server.HttpServer;
import black.door.net.http.server.Route;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;
import black.door.net.http.server.controllers.BasicControllerFactory;


import java.io.File;
import java.util.regex.Pattern;

import static black.door.util.DBP.enableChannel;

/**
 * This abomination created by nfischer on 7/9/2015.
 */
public class Main {
	public static void main(String[] args){
		enableChannel("debug");
		enableChannel("demo");

		File cwd = new File(System.getProperty("user.dir"));
		System.out.println(cwd);

		Route all = new Route(Pattern.compile("^/assets/?(.*)$"), BasicControllerFactory.getInstance(FileController.class));

		HttpServer server = new HttpServer();

		server.addBehavior(HttpVerb.GET, "/hello", (request, params)->{
			HttpResponse response = StandardResponses.OK.getResponse();
			response.setBody("Hello world".getBytes());
			return response;
		});

		server.addRoute(all);

		server.run();

	}

}
