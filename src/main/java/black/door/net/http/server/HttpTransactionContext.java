package black.door.net.http.server;

import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;

/**
 * This abomination created by nfischer on 7/9/2015.
 *
 * The context of an HTTP transaction consists of the request and response.
 * HttpTransactionContext can be used to see the request and current response at any point in the transaction.
 */
public class HttpTransactionContext {
	private HttpRequest request;
	private HttpResponse response;

	protected HttpTransactionContext(){}

	public HttpTransactionContext(HttpRequest request, HttpResponse response){
		this.request = request;
		this.response = response;
	}

	public HttpRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpRequest request) {
		this.request = request;
	}

	public HttpResponse getResponse() {
		return response;
	}

	protected void setResponse(HttpResponse response) {
		this.response = response;
	}

}
