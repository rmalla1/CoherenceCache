import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.core.io.Resource;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.Cluster;
import com.tangosol.net.DefaultConfigurableCacheFactory;

/**
 * This follows EhCache implementation
 *
 * @author: Ratnakar Malla
 */
public class CoherenceCacheManagerFactoryBean implements FactoryBean<CacheManager>, InitializingBean, DisposableBean {


	protected final Log logger = LogFactory.getLog(getClass());

	private Resource configLocation;

	private boolean shared = false;

	private String cacheManagerName;

	private CacheManager cacheManager;


	/**
	 * Set the location of the EhCache config file. A typical value is "/WEB-INF/ehcache.xml".
	 * <p>Default is "ehcache.xml" in the root of the class path, or if not found,
	 * "ehcache-failsafe.xml" in the EhCache jar (default EhCache initialization).
	 * @see net.sf.ehcache.CacheManager#create(java.io.InputStream)
	 * @see net.sf.ehcache.CacheManager#CacheManager(java.io.InputStream)
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * Set whether the EhCache CacheManager should be shared (as a singleton at the VM level)
	 * or independent (typically local within the application). Default is "false", creating
	 * an independent instance.
	 * @see net.sf.ehcache.CacheManager#create()
	 * @see net.sf.ehcache.CacheManager#CacheManager()
	 */
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	/**
	 * Set the name of the EhCache CacheManager (if a specific name is desired).
	 * @see net.sf.ehcache.CacheManager#setName(String)
	 */
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}


	@SuppressWarnings("deprecation")
	public void afterPropertiesSet() throws IOException {
		logger.info("Initializing Coherence CacheManager");
		InputStream is = (this.configLocation != null ? this.configLocation.getInputStream() : null);
		try {

			Cluster cluster=CacheFactory.ensureCluster();
			CacheFactory.setConfigurableCacheFactory(new DefaultConfigurableCacheFactory("coherence-cache-config.xml"));
			CacheFactory.getCache("dummy");
			this.cacheManager = new CoherenceCacheManager();

		}
		finally {
			if (is != null) {
				is.close();
			}
		}
	}


	public CacheManager getObject() {
		return this.cacheManager;
	}

	public Class<? extends CacheManager> getObjectType() {
		return (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
	}

	public boolean isSingleton() {
		return true;
	}


	public void destroy() {
		logger.info("Shutting down CacheManager");
		//this.cacheManager.shutdown();
	}

}
