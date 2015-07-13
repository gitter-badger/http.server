package black.door.net.http.server;

import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;

import java.util.*;

/**
 * This abomination created by cjbur on 7/9/2015.
 */
public class FilterContainer {

    private Map<HttpVerb,List<Filter>> preControl;
    private Map<HttpVerb,List<Filter>> postControl;
    private HttpRequest request;

    public FilterContainer() {
        this.request = request;
    }

    public HttpResponse preControl() {
        return null;
    }

    public HttpResponse postControl() {
        return null;
    }

    public static abstract class FilterContainerFactory {
        
    }

}
