import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This abomination created by nfischer on 7/9/2015.
 */
public class Messing {
	@Test
	public void messing() throws URISyntaxException, MalformedURLException {
		URI uri = new URI("https://www.google.com/search?q=search&oq=search&aqs=chrome..69i57j69i60l2j69i65l3.3951j0j7&sourceid=chrome&es_sm=93&ie=UTF-8");
		System.out.println(uri.toURL().getPath());
	}
}
