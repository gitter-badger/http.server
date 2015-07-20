package black.door.net.http.server.example;

import black.door.net.http.server.annotations.Controller;
import black.door.net.http.server.annotations.HttpMethod;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;

/**
 * Created by nfischer on 7/17/2015.
 */
@Controller("^/world$")
public class HelloController {

	@HttpMethod(HttpVerb.GET)
	public HttpResponse fn(HttpRequest request, String... params){
		HttpResponse response = StandardResponses.OK.getResponse();
		response.setBody("World".getBytes());
		return response;
	}

	@HttpMethod(HttpVerb.POST)
	public HttpResponse fn2(HttpRequest request, String... params){
		HttpResponse response = StandardResponses.OK.getResponse();
		response.setBody("POST World".getBytes());
		return response;
	}
}
