package black.door.net.http.server;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * This abomination created by nfischer on 7/9/2015.
 *
 * The Router determines which route should be used for a certain path.
 * Routes are considered in the order in which they were added to the Router, or equivalently, the order in which they appear in the routes List.
 * It is recommended that the anchors ^ and $ are used to in Route regex. If anchors are not used then routes should be added in order of decreasing specificity.
 *      This way paths like /things/thing and /things are not sent to the wrong Route and Controller.
 */
public class Router {
	private List<Route> routes;

	public Router(){
		routes = new LinkedList<>();
	}

	public Router addRoute(Route route){
		routes.add(route);
		return this;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public Route getRoute(String path){
		for(Route route :routes){

			Matcher matcher = route.getPath().matcher(path);
			if(matcher.matches())
				return route;
		}
		return null;
	}
}
