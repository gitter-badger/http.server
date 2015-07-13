package black.door.net.http.server;

import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;

/**
 * This abomination created by nfischer on 7/9/2015.
 * <p>
 * Controllers define the behavior for HTTP verbs at specific paths. To create a new controller extend the Controller
 * class and override the method corresponding to the HTTP verbs you want to handle.
 * <p>
 * The parameters for the methods in this class are path parameters from the Route classes added to the Router
 */
public abstract class Controller implements ContextualElement {
    private HttpTransactionContext context;

    public HttpRequest getRequest() {
        return context.getRequest();
    }

    public HttpTransactionContext getContext() {
        return context;
    }

    void setContext(HttpTransactionContext context) {
        this.context = context;
    }

    HttpResponse control(String... params) {
        switch (getRequest().getVerb()) {
            case OPTIONS:
                return options(params);
            case GET:
                return get(params);
            case HEAD:
                return head(params);
            case POST:
                return post(params);
            case PUT:
                return put(params);
            case DELETE:
                return delete(params);
            case TRACE:
                return trace(params);
            case CONNECT:
                return connect(params);
            default:
                return StandardResponses.BAD_REQUEST.getResponse();
        }
    }

    public HttpResponse options(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse get(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse head(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse post(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse put(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse delete(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse trace(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    public HttpResponse connect(String... params) {
        return StandardResponses.NOT_FOUND.getResponse();
    }

    /**
     * The ControllerFactory class instructs the server on how to make Controllers of different types.
     * Extend ControllerFactory to add data from your application's context (such as DB connection pools or service and API keys) to built Controllers.
     */
    public interface ControllerFactory {
        Controller manufacture();
    }
}
