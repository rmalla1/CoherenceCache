import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Cluster;
import com.tangosol.net.NamedCache;

public class CoherenceInitListener implements ServletContextListener {


	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("CoherenceInitListener destroyed");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("CoherenceInitListener started");
		Cluster cluster=CacheFactory.ensureCluster();
		NamedCache nCache = CacheFactory.getCache("dummy");
		nCache.put("Name", "GI");
		System.out.println("Reading from Cache: " + nCache.get("Name"));
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
