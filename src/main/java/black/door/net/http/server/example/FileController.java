package black.door.net.http.server.example;

import black.door.net.http.server.Controller;
import black.door.net.http.server.annotations.PreFilter;
import black.door.net.http.server.responses.StandardResponses;
import black.door.net.http.tools.HttpResponse;
import black.door.util.DBP;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static black.door.util.DBP.printdebugln;

/**
 * This abomination created by nfischer on 7/7/2015.
 */

public class FileController extends Controller {

	Path root= new File(System.getProperty("user.dir")).toPath();

	public HttpResponse get(String... params){

		Path requestedFile = root.resolve(params[0]);

		if(!requestedFile.toFile().exists())
			return StandardResponses.NOT_FOUND.getResponse();

		if(requestedFile.toFile().isDirectory()){
			HttpResponse response = StandardResponses.OK.getResponse();
			String path = getRequest().getUri().getPath();
			String host = getRequest().getHeaders().get("Host");
			String webpath = (host == null ? "" : host) + path + (path.endsWith("/") ? "" : '/');
			printdebugln(webpath);
			StringBuilder bod = new StringBuilder();
			for(File file : requestedFile.toFile().listFiles()){
				bod.append(webpath);
				bod.append(file.getName());
				if(file.isDirectory())
					bod.append('/');
				bod.append('\n');
			}
			response.setBody(bod.toString().getBytes());
			return response;
		}


		HttpResponse response = StandardResponses.OK.getResponse();
		try {
			response.setBody(Files.readAllBytes(requestedFile));
		} catch (IOException e) {
			response = StandardResponses.SERVER_ERROR.getResponse();
		}

		return response;
	}

	@PreFilter(NullAuthFilter.class)
	public HttpResponse put(String... params){
		Path requestedFile = root.resolve(params[0]);
		if(requestedFile.toFile().isDirectory()){
			return StandardResponses.NOT_FOUND.getResponse();
		}
		try {
			Files.write(requestedFile, getRequest().getBody(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			printdebugln(e);
			return StandardResponses.SERVER_ERROR.getResponse();
		}
		return StandardResponses.OK.getResponse();
	}

}
