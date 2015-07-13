package black.door.net.http.server.controllers;

import black.door.net.http.server.Controller;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;

import java.util.Map;

/**
 * Created by nfischer on 7/12/2015.
 */
public class FunctionalController extends Controller{
	private Map<HttpVerb, HttpFunction> functions;

	public HttpResponse options(String... params) {
		return method(HttpVerb.OPTIONS , params);
	}

	public HttpResponse get(String... params) {
		return method(HttpVerb.GET, params);
	}

	public HttpResponse head(String... params) {
		return method(HttpVerb.HEAD, params);
	}

	public HttpResponse post(String... params) {
		return method(HttpVerb.POST, params);
	}

	public HttpResponse put(String... params) {
		return method(HttpVerb.PUT, params);
	}

	public HttpResponse delete(String... params) {
		return method(HttpVerb.DELETE, params);
	}

	public HttpResponse trace(String... params) {
		return method(HttpVerb.TRACE, params);
	}

	public HttpResponse connect(String... params) {
		return method(HttpVerb.CONNECT, params);
	}

	private HttpResponse method(HttpVerb method, String... params){
		if(!functions.containsKey(method))
			return StandardResponses.NOT_FOUND.getResponse();
		return functions.get(method).fn(getRequest(), params);
	}

	public static ControllerFactory getFactory(Map<HttpVerb, HttpFunction> functions){
		return () -> {
			FunctionalController controller = new FunctionalController();
			controller.functions = functions;
			return controller;
		};
	}
}
