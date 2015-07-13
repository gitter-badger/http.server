package black.door.net.http.server;

import black.door.net.http.server.controllers.FunctionalController;
import black.door.net.http.server.controllers.HttpFunction;
import black.door.net.http.tools.HttpVerb;
import black.door.net.server.Server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by nfischer on 7/11/2015.
 */
public class HttpServer implements Runnable {

	private Router router;
	private Server server;
	private int port;

	private Map<Pattern, Map<HttpVerb, HttpFunction>> functionalRoutes;

	public HttpServer(){
		this(80);
	}

	public HttpServer(int port){
		this.port = port;
		router = new Router();
		functionalRoutes = new HashMap<>();
	}

	public Router getRouter(){
		Router router = new Router();

		composeFunctionalRoutes().forEach(e -> router.addRoute(e));

		router.getRoutes().addAll(this.router.getRoutes());

		return router;
	}

	public HttpServer addRoute(Route route) {
		router.addRoute(route);
		return this;
	}

	/**
	 * Operations added through this method take precedence over ones added using addRoute(Route).
	 * @param method
	 * @param path
	 * @param behavior
	 * @return
	 */
	public HttpServer addBehavior(HttpVerb method, String path, HttpFunction behavior){
		Map<HttpVerb, HttpFunction> endpoint;
		Pattern pattern = Pattern.compile(path);
		if(functionalRoutes.containsKey(pattern)){
			endpoint = functionalRoutes.get(pattern);
		}else{
			endpoint = new HashMap<>();
		}
		endpoint.put(method, behavior);
		functionalRoutes.put(pattern, endpoint);

		return this;
	}

	private List<Route> composeFunctionalRoutes(){
		List<Route> routeList = new LinkedList<>();

		functionalRoutes.entrySet().forEach(e ->{
			Route route = new Route(e.getKey(), FunctionalController.getFactory(e.getValue()));
			routeList.add(route);
		});

		return routeList;
	}

	@Override
	public void run() {
		HttpServerThread.HttpServerThreadBuilder threadBuilder = new HttpServerThread.HttpServerThreadBuilder(getRouter());
		server = new Server(threadBuilder, port);
		server.run();
	}

	public void shutDown(){
		server.stop();
	}
}
