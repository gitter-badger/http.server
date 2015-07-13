package black.door.net.http.server.example;

import black.door.net.http.server.Filter;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpResponse;

/**
 * Created by nfischer on 7/11/2015.
 */
public class NullAuthFilter extends Filter {

	@Override
	public HttpResponse filter() {
		if(getContext().getRequest().getHeaders().containsKey("Authorization")){
			return null;
		}
		return StandardResponses.UNAUTHORIZED.getResponse();
	}
}
