package black.door.net.http.server.example;

import black.door.net.http.server.HttpServer;
import black.door.net.http.server.Route;
import black.door.net.http.server.controllers.BasicControllerFactory;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;


import java.io.File;
import java.util.regex.Pattern;

import static black.door.util.DBP.enableChannel;
import static black.door.util.DBP.printdemoln;

/**
 * This abomination created by nfischer on 7/9/2015.
 */
public class Main {
	public static void main(String[] args){
		enableChannel("debug");
		enableChannel("demo");

		File cwd = new File(System.getProperty("user.dir"));
		System.out.println(cwd);

		/**
		FilterContainerFactory fc;
		fc.addPre(HttpVerb.GET, new MyFilter{});

		fc.addPost(HttpVerb.PUT, new MyFilter{){
			//filter bs
		});
		 */


		Route all = new Route(Pattern.compile("^\\/(.*)$"), BasicControllerFactory.getInstance(FileController.class));

		HttpServer server = new HttpServer();

		server.addBehavior(HttpVerb.GET, "\\/hello", (request, params)->{
			printdemoln(request);
			HttpResponse response = StandardResponses.OK.getResponse();
			response.setBody("Hello world".getBytes());
			return response;
		});

		server.addRoute(all);

		server.run();

	}

}
