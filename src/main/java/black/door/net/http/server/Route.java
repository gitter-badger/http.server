package black.door.net.http.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abomination created by nfischer on 7/9/2015.
 *
 * Routes represent a mapping from a URL path to some behavior defined in a Controller.
 * Paths in a route are defined using regular expressions. For example, a Route with the regex /^\/people\/(\w+)$/
 * could be used to indicate that the path /people/jim should be handled by this route, and that the path has one parameter: jim.
 * It is recommended that the ^ and $ anchors are used in regex here.
 */
public class Route {
	private Pattern path;
	private Controller.ControllerFactory controllerFactory;

	public Route(Pattern path, Controller.ControllerFactory controllerFactory){
		this.path = path;
		this.controllerFactory = controllerFactory;
	}

	public Pattern getPath() {
		return path;
	}

	public void setPath(Pattern path) {
		this.path = path;
	}

	public Controller.ControllerFactory getControllerFactory() {
		return controllerFactory;
	}

	public Controller getController(){
		return controllerFactory.manufacture();
	}

	/**
	 * Returns an array of the parameters found in path for the regex defined for this Route.
	 * For example a Route with the regex /^\/users\/(\w+)\/groups\/(\w+)$/ would return ["alice", "friends"] for the path "/users/alice/groups/friends".
	 * @param path
	 * @return
	 */
	public String[] getPathParams(String path){
		String[] params;
		Matcher matcher = this.path.matcher(path);
		matcher.find();
		params = new String[matcher.groupCount()];
		for(int i = 0; i < params.length; i++){
			params[i] = matcher.group(i +1);
		}
		return params;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Route route = (Route) o;

		return path.equals(route.path);

	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public String toString() {
		return "Route{" +
				"path=" + path +
				'}';
	}
}
