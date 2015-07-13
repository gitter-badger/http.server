package black.door.net.http.server.responses;

import black.door.net.http.tools.HttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * Created by nfischer on 7/7/2015.
 */
public enum StandardResponses {
	CONTINUE(100, "Continue"),  OK(200, "OK"), BAD_REQUEST(400, "Bad Request"), UNAUTHORIZED(401, "Unauthorized"), NOT_FOUND(404, "Not Found"),  TEAPOT(418, "I'm a teapot"), SERVER_ERROR(500, "Server Error");
	private static final String VERSION = "http/1.1";
	private int statusCode;
	private String message;

	StandardResponses(int statusCode, String message){
		this.statusCode = statusCode;
		this.message = message;
	}

	public static HttpResponse getResponse(StandardResponses response){
		return response.getResponse();
	}

	public HttpResponse getResponse(){
		HttpResponse response = new HttpResponse(VERSION, statusCode, message);
		response.putHeader("Content-Type", "text/plain");
		response.setBody((String.valueOf(statusCode) + " " + message).getBytes(StandardCharsets.UTF_8));
		return response;
	}
}
