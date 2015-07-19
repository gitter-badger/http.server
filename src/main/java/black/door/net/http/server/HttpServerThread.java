package black.door.net.http.server;

import black.door.net.http.server.annotations.PostFilter;
import black.door.net.http.server.annotations.PreFilter;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.server.singletons.Config;
import black.door.net.http.tools.HttpParsingException;
import black.door.net.http.tools.HttpRequest;
import black.door.net.http.tools.HttpResponse;
import black.door.net.server.ServerThread;
import org.apache.commons.io.input.BoundedInputStream;

import static black.door.util.DBP.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;

/**
 * This abomination created by nfischer on 7/9/2015.
 */
class HttpServerThread implements ServerThread, ContextualElement{
    private Socket sock;
    private Router router;
    private HttpTransactionContext context;
    private static final int SOCKET_TIMEOUT = 7 * 1000;

    public HttpServerThread() {
        context = new HttpTransactionContext();
    }

    public Socket getSocket() {
        return sock;
    }

    public void run() {
        try {
            try {
                sock.setSoTimeout(SOCKET_TIMEOUT);

                while (true) {
                    transaction();
                }

            } catch (SocketTimeoutException sTO) {
            }

            sock.close();
        }catch (Exception e) {}
    }

    private void transaction() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        context = new HttpTransactionContext();
        HttpRequest request;
        HttpResponse response;
        Controller controller;
        Route route;
        InputStream is;

        try {
            try {
                is = new BoundedInputStream(new BufferedInputStream(sock.getInputStream()), Config.INSTANCE.getMaxRequestSize());
                request = HttpRequest.parse(is, Config.INSTANCE.getMaxRequestSize());
                context.setRequest(request);
            } catch (URISyntaxException | HttpParsingException e) {
                printException(e);
                context.setResponse(StandardResponses.BAD_REQUEST.getResponse());
                sendResponse();
                return;
            }

            printdemoln(request);

            route = router.getRoute(request.getUri().getPath());

            if (route == null) {
                context.setResponse(StandardResponses.NOT_FOUND.getResponse());
                sendResponse();
                return;
            }

            controller = route.getController();
            controller.setContext(context);

            if (runPreFilters(controller.getClass())) {
                sendResponse();
                return;
            }

            response = controller.control(route.getPathParams(request.getUri().getPath()));
            context.setResponse(response);

            runPostFilters(controller.getClass());

            sendResponse();

        }catch (EOFException e){
            throw e;
        } catch (IOException to) {
            printException(to);
            throw to;
        } catch (Exception e) {
            printException(e);
            context.setResponse(StandardResponses.SERVER_ERROR.getResponse());
            sendResponse();
            throw e;
        }
    }

	private void runPostFilters(Class<? extends Controller> controllerClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		//region run filters for entire path
		runPostFiltersForElement(controllerClass);
		//endregion

		//region run filters for method
		Method method = controllerClass.getMethod(getContext().getRequest().getVerb().name().toLowerCase(), String[].class);
		runPostFiltersForElement(method);
		//endregion
	}

	private boolean runPostFiltersForElement(AnnotatedElement e) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		Annotation[] filters = e.getAnnotationsByType(PostFilter.class);

		for(Annotation annotation :filters){
			PostFilter filterAnnotation = (PostFilter) annotation;
			Class filterClass = filterAnnotation.value();
			Filter instance = (Filter) getZeroParamConstructor(filterClass).newInstance();
			instance.setContext(this.context);

			HttpResponse response = instance.filter();
			if(response != null) {
				this.context.setResponse(response);
			}
		}

		return false;
	}

	private boolean runPreFiltersForElement(AnnotatedElement e) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		Annotation[] filters = e.getAnnotationsByType(PreFilter.class);

		for(Annotation annotation :filters){
			PreFilter filterAnnotation = (PreFilter) annotation;
			Class filterClass = filterAnnotation.value();
			Filter instance = (Filter) getZeroParamConstructor(filterClass).newInstance();
			instance.setContext(this.context);

			HttpResponse response = instance.filter();
			if(response != null) {
				this.context.setResponse(response);
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @param controllerClass
	 * @return true if one of the triggers has caused a premature response (i.e. the controller should not be run and a response should be sent now)
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private boolean runPreFilters(Class<? extends Controller> controllerClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		if(runPreFiltersForElement(controllerClass))
			return true;

		Method method = controllerClass.getMethod(getContext().getRequest().getVerb().name().toLowerCase(), String[].class);
		if(runPreFiltersForElement(method))
			return true;

		return false;
	}

	/**
	 *
	 * @param c
	 * @return the zero parameter constructor for c (or null if something weird happens)
	 * @throws SecurityException if the zero param constructor could not be made accessible
	 */
	protected static Constructor getZeroParamConstructor(Class c) throws SecurityException, NoSuchMethodException {
		Constructor[] constructors = c.getConstructors();

		for(Constructor con :constructors){
			if(con.getParameterCount() == 0){
				if(! con.isAccessible()){
					con.setAccessible(true);
				}
				return con;
			}
		}

        throw new NoSuchMethodException("Could not find a zero parameter constructor for " + c.getCanonicalName());
	}

    private void sendResponse() throws IOException {

        OutputStream out = sock.getOutputStream();
        BufferedOutputStream outStream = new BufferedOutputStream(out);
        HttpResponse response = context.getResponse();
        response.putHeader("Content-Length", response.getBody() == null ? "0" : String.valueOf(response.getBody().length));
        printdemoln(context.getResponse());
        outStream.write(context.getResponse().serialize());
        outStream.flush();
    }

	@Override
	public HttpTransactionContext getContext() {
		return context;
	}

	public static class HttpServerThreadBuilder implements ServerThreadBuilder {
        private Router router;

        public HttpServerThreadBuilder(Router router) {
            this.router = router;
        }

        public ServerThread build(Socket socket) {
            HttpServerThread thread = new HttpServerThread();
            thread.router = router;
            thread.sock = socket;
            return thread;
        }
    }
}
