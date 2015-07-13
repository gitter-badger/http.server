package black.door.net.http.server.controllers;

import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;

/**
 * Created by nfischer on 7/12/2015.
 */
public interface HttpFunction {
	public HttpResponse fn(HttpRequest request, String... params);
}
