package black.door.net.http.server;

import black.door.net.http.tools.HttpResponse;

/**
 * This abomination created by nfischer on 7/9/2015.
 */
public abstract class Filter implements ContextualElement {

	private HttpTransactionContext context;

	public Filter(){}

	/**
	 * Filter a http transaction.
	 * Request filters will run before the request is handled, so the response part of the transaction will be null.
	 * Response filters will run after the transaction is handled.
	 * If a request filter has a non-null return value, then the returned request will not be handled and the response filters will be run.
	 * All response filters will always run. Returning non-null will change the HttpTransactionContext that the next filter recieves.
	 * @return the HTTP response to send back or null if this filter does not change the response in any way.
	 */
	public abstract HttpResponse filter();

	public HttpTransactionContext getContext(){
		return this.context;
	}

	void setContext(HttpTransactionContext context){
		this.context = context;
	}
}
