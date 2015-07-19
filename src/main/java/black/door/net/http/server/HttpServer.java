package black.door.net.http.server;

import black.door.net.http.server.annotations.*;
import black.door.net.http.server.annotations.Controller;
import black.door.net.http.server.controllers.FunctionalController;
import black.door.net.http.server.controllers.HttpFunction;
import black.door.net.http.tools.HttpResponse;
import black.door.net.http.tools.HttpVerb;
import black.door.net.server.Server;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by nfischer on 7/11/2015.
 */
public class HttpServer implements Runnable {

	private Router router;
	private Server server;
	private int port;

	private Map<Pattern, Map<HttpVerb, HttpFunction>> functionalRoutes;

	public HttpServer(){
		this(80);
	}

	public HttpServer(int port){
		this.port = port;
		router = new Router();
		functionalRoutes = new LinkedHashMap<>();
	}

	public Router getRouter(){
		Router router = new Router();

		router.getRoutes().addAll(this.router.getRoutes());

		findAnnotatedRoutes();

		composeFunctionalRoutes().forEach(e -> router.addRoute(e));

		return router;
	}

	public HttpServer addRoute(Route route) {
		router.addRoute(route);
		return this;
	}

	/**
	 * Operations added through this method take precedence after ones added using addRoute(Route).
	 * @param method
	 * @param path
	 * @param behavior
	 * @return
	 */
	public HttpServer addBehavior(HttpVerb method, String path, HttpFunction behavior){
		Map<HttpVerb, HttpFunction> endpoint;
		Pattern pattern = Pattern.compile(path);
		if(functionalRoutes.containsKey(pattern)){
			endpoint = functionalRoutes.get(pattern);
		}else{
			endpoint = new HashMap<>();
		}
		endpoint.put(method, behavior);
		functionalRoutes.put(pattern, endpoint);

		return this;
	}

	private void findAnnotatedRoutes(){
		Reflections reflections = new Reflections();
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);

		annotatedClasses.forEach(e -> {
			String path = e.getAnnotation(Controller.class).value();
			Reflections r = new Reflections(new ConfigurationBuilder().addScanners(new MethodAnnotationsScanner()).setUrls(Arrays.asList(ClasspathHelper.forClass(e))));//(e);

			Set<Method> methods = r.getMethodsAnnotatedWith(HttpMethod.class);

			Constructor c = null;
			try {
				c = HttpServerThread.getZeroParamConstructor(e);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				throw new RuntimeException("Could not get 0 param constructor of " + e.getCanonicalName());
			}
			Object thingy = null;
			try {
				thingy = c.newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e1) {
				e1.printStackTrace();
				throw new RuntimeException("Could not invoke 0 param constructor of " + e.getCanonicalName());
			}
			final Object finalThingy = thingy;
			methods.forEach(m -> {
				HttpVerb verb = m.getAnnotation(HttpMethod.class).value();

				addBehavior(verb, path, (request, params) -> {
					try {
						return (HttpResponse) (m.invoke(finalThingy, request, params));
					} catch (IllegalAccessException | InvocationTargetException e1) {
						e1.printStackTrace();
						throw new RuntimeException("Parameters of " + e.getCanonicalName() + '.' + m.getName() +" do not match (HttpRequest, String...)");
					}
				});
			});
		});

	}

	private List<Route> composeFunctionalRoutes(){
		List<Route> routeList = new LinkedList<>();

		functionalRoutes.entrySet().forEach(e ->{
			Route route = new Route(e.getKey(), FunctionalController.getFactory(e.getValue()));
			routeList.add(route);
		});

		return routeList;
	}

	@Override
	public void run() {
		HttpServerThread.HttpServerThreadBuilder threadBuilder = new HttpServerThread.HttpServerThreadBuilder(getRouter());
		server = new Server(threadBuilder, port);
		server.run();
	}

	public void shutDown(){
		server.stop();
	}
}
