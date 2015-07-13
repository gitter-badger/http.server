package black.door.net.http.server.controllers;

import black.door.net.http.server.Controller;

import static black.door.util.DBP.*;

/**
 * This abomination created by nfischer on 7/9/2015.
 * <p>
 * voodoo magic
 */
public class BasicControllerFactory implements Controller.ControllerFactory{

	private Class<? extends Controller> controllerClass;

	public BasicControllerFactory(Class<? extends Controller> controllerClass){
		this.controllerClass = controllerClass;
	}

	@Override
	public Controller manufacture() {
		Controller controller = null;
		try {
			controller = controllerClass.newInstance();
		} catch (InstantiationException e) {
			printException(e);
		} catch (IllegalAccessException e) {
			printException(e);
		}
		return controller;
	}

	public static Controller.ControllerFactory getInstance(Class<? extends Controller> controllerClass){
		return () -> {
			try {
				return controllerClass.newInstance();
			} catch (InstantiationException e) {
				printException(e);
			} catch (IllegalAccessException e) {
				printException(e);
			}
			return null;
		};
	}
}
