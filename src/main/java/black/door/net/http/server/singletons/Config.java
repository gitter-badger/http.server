package black.door.net.http.server.singletons;

/**
 * Created by nfischer on 7/12/2015.
 */
public enum Config {
	INSTANCE;

	private int maxRequestSize = 8192;

	Config(){}

	public int getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}
}
